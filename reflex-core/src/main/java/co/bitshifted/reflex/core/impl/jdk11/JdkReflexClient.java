/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.impl.jdk11;

import static co.bitshifted.reflex.core.Reflex.*;
import static co.bitshifted.reflex.core.Reflex.context;

import co.bitshifted.reflex.core.ReflexClient;
import co.bitshifted.reflex.core.config.ReflexClientConfiguration;
import co.bitshifted.reflex.core.exception.HttpClientException;
import co.bitshifted.reflex.core.exception.HttpStatusException;
import co.bitshifted.reflex.core.http.RFXHttpHeaders;
import co.bitshifted.reflex.core.http.RFXHttpRequest;
import co.bitshifted.reflex.core.http.RFXHttpResponse;
import co.bitshifted.reflex.core.http.RFXHttpStatus;
import co.bitshifted.reflex.core.impl.Helper;
import co.bitshifted.reflex.core.serialize.BodySerializer;
import co.bitshifted.reflex.core.ssl.TrustAllTrustManager;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdkReflexClient implements ReflexClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(JdkReflexClient.class);

  private final HttpClient httpClient;
  private final ReflexClientConfiguration configuration;
  private BodySerializer dataSerializer;

  public JdkReflexClient() {
    this(context().configuration());
  }

  public JdkReflexClient(ReflexClientConfiguration config) {
    this.configuration = config;
    var jdk11Config = Jdk11ConfigConverter.fromConfig(config);

    var sslContext = getSslContext(config.disableSslCertVerification());
    var sslParams = sslContext.getDefaultSSLParameters();
    sslParams.setEndpointIdentificationAlgorithm(null);
    this.httpClient =
        HttpClient.newBuilder()
            .connectTimeout(jdk11Config.connectTimeout())
            .followRedirects(jdk11Config.redirectPolicy())
            .version(jdk11Config.httpVersion())
            .sslContext(sslContext)
            .build();
  }

  @Override
  public <T> RFXHttpResponse sendHttpRequest(RFXHttpRequest<T> request)
      throws HttpClientException, HttpStatusException {
    var jdkHttpRequest = createHttpRequest(request);
    LOGGER.debug("Request URL: {}", jdkHttpRequest.uri().toString());
    try {
      var response = httpClient.send(jdkHttpRequest, HttpResponse.BodyHandlers.ofInputStream());
      if (response.statusCode() >= RFXHttpStatus.BAD_REQUEST.code()) {
        throw new HttpStatusException("HTTP error status code: " + response.statusCode());
      }
      var contentType = response.headers().firstValue(RFXHttpHeaders.CONTENT_TYPE);
      Optional<BodySerializer> bodySerializer;
      if (contentType.isPresent()) {
        LOGGER.debug("Response content type: {}", contentType.get());
        bodySerializer = context().getSerializerFor(contentType.get());
      } else {
        LOGGER.debug("No Content-Type header found in response");
        bodySerializer = Optional.empty();
      }
      if (bodySerializer.isPresent()) {
        LOGGER.debug("Found response body serializer {}", bodySerializer.get());
      }
      var responseHeaders = new RFXHttpHeaders();
      response
          .headers()
          .map()
          .forEach((key, values) -> values.forEach(v -> responseHeaders.setHeader(key, v)));
      return new RFXHttpResponse(
          RFXHttpStatus.findByCode(response.statusCode()),
          Optional.of(response.body()),
          bodySerializer,
          Optional.of(responseHeaders));
    } catch (IOException ex) {
      LOGGER.error("Failed to send HTTP request", ex);
      throw new HttpClientException(ex);
    } catch (InterruptedException ex) {
      LOGGER.error("Failed to send HTTP request", ex);
      Thread.currentThread().interrupt();
      throw new HttpClientException(ex);
    }
  }

  @Override
  public <T> CompletableFuture<RFXHttpResponse> sendHttpRequestAsync(RFXHttpRequest<T> request) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            return sendHttpRequest(request);
          } catch (Exception ex) {
            throw new CompletionException(ex);
          }
        });
  }

  private <T> HttpRequest createHttpRequest(RFXHttpRequest<T> request) throws HttpClientException {
    var publisher = getRequestBodyPublisher(request);
    var reqBuilder =
        HttpRequest.newBuilder(Helper.calculateUri(request, configuration.baseUri()))
            .method(request.method().name(), publisher);
    var commonHeaders = context().configuration().commonHeaders();
    commonHeaders
        .entrySet()
        .forEach(entry -> reqBuilder.setHeader(entry.getKey(), entry.getValue()));
    var requestHeaders = request.headers().orElse(new RFXHttpHeaders());
    var allHeaders = requestHeaders.getAllHeaders();
    allHeaders
        .keySet()
        .forEach(
            k -> {
              allHeaders.get(k).forEach(v -> reqBuilder.setHeader(k, v));
            });
    return reqBuilder.build();
  }

  private <T> HttpRequest.BodyPublisher getRequestBodyPublisher(RFXHttpRequest<T> request)
      throws HttpClientException {
    var publisher = HttpRequest.BodyPublishers.noBody();
    if (request.body().isPresent()) {
      var contentType =
          request
              .headers()
              .orElseThrow(() -> new HttpClientException("Content-Type header is not present"))
              .getHeaderValue(RFXHttpHeaders.CONTENT_TYPE)
              .orElseThrow(() -> new HttpClientException("Request content type not specified"));
      var bodySerializer =
          context()
              .getSerializerFor(contentType.get(0))
              .orElseThrow(
                  () ->
                      new HttpClientException(
                          "No body serializer found for content type " + contentType.get(0)));
      publisher =
          HttpRequest.BodyPublishers.ofInputStream(
              () -> bodySerializer.objectToStream(request.body().get()));
    }
    return publisher;
  }

  private SSLContext getSslContext(boolean disableSslCertValidation) {
    try {
      if (disableSslCertValidation) {
        LOGGER.warn(
            "SSL certificate validation is disabled. This is insecure and should not be used in production.");
        var tm = new TrustManager[] {new TrustAllTrustManager()};
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, tm, new SecureRandom());
        return ctx;
      } else {
        return SSLContext.getDefault();
      }
    } catch (NoSuchAlgorithmException | KeyManagementException ex) {
      throw new RuntimeException(ex);
    }
  }
}
