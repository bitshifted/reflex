/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.impl.urlconn;

import co.bitshifted.reflex.core.Reflex;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class HttpUrlConnectionClient implements ReflexClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpUrlConnectionClient.class);

  private HttpUrlConnectionClientConfig config;

  public HttpUrlConnectionClient() {
    var defaultConfig = Reflex.context().configuration();
    this.config = HttpUrlConnectionConfigConverter.fromConfig(defaultConfig);
  }

  public HttpUrlConnectionClient(ReflexClientConfiguration configuration) {
    this.config = HttpUrlConnectionConfigConverter.fromConfig(configuration);
  }

  @Override
  public <T> RFXHttpResponse sendHttpRequest(RFXHttpRequest<T> request)
      throws HttpClientException, HttpStatusException {
    try {
      var url = Helper.calculateUri(request).toURL();
      LOGGER.debug("Target URL: {}", url);
      var urlConn = (HttpURLConnection) url.openConnection();
      urlConn.setRequestMethod(request.method().name());
      urlConn.setConnectTimeout((int) config.connectTimeout());
      urlConn.setReadTimeout((int) config.readTimeout());
      urlConn.setInstanceFollowRedirects(config.redirect());
      var commonHeaders = Reflex.context().configuration().commonHeaders();
      commonHeaders
          .entrySet()
          .forEach(entry -> urlConn.setRequestProperty(entry.getKey(), entry.getValue()));
      if (request.headers().isPresent()) {
        var allHeaders = request.headers().get().getAllHeaders();
        allHeaders.keySet().stream()
            .forEach(
                headerName -> {
                  var values = allHeaders.get(headerName);
                  urlConn.setRequestProperty(headerName, concatenate(values));
                });
      }
      urlConn.setDoInput(true);
      if (request.body().isPresent()) {
        var requestBodySerializer =
            getBodySerializer(
                request.headers().get().getHeaderValue(RFXHttpHeaders.CONTENT_TYPE).get().get(0));
        urlConn.setDoOutput(true);
        urlConn
            .getOutputStream()
            .write(requestBodySerializer.get().objectToStream(request.body().get()).readAllBytes());
      }

      urlConn.connect();
      var statusCode = urlConn.getResponseCode();
      if (statusCode >= RFXHttpStatus.BAD_REQUEST.code()) {
        throw new HttpStatusException("HTTP error status: " + statusCode);
      }
      Optional<BodySerializer> bodySerializer = Optional.empty();
      if (urlConn.getContentType() != null && !urlConn.getContentType().isEmpty()) {
        bodySerializer = getBodySerializer(urlConn.getContentType());
      }
      return new RFXHttpResponse(
          RFXHttpStatus.findByCode(statusCode),
          getResponseBody(urlConn),
          bodySerializer,
          Optional.empty());
    } catch (MalformedURLException ex) {
      throw new HttpClientException("Malformed URL: " + request.uri().toString());
    } catch (IOException ex) {
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

  private String concatenate(List<String> list) {
    if (list.isEmpty()) {
      return "";
    }
    var sb = new StringBuilder(list.get(0));
    for (int i = 1; i < list.size(); i++) {
      sb.append(",").append(list.get(i));
    }
    return sb.toString();
  }

  private Optional<BodySerializer> getBodySerializer(String mimeType) {
    return Reflex.context().getSerializerFor(mimeType);
  }

  private Optional<InputStream> getResponseBody(HttpURLConnection conn) throws IOException {
    var in = conn.getInputStream();
    if (in.available() > 0) {
      return Optional.of(in);
    }
    return Optional.empty();
  }
}
