/*
 *
 *  * Copyright (c) 2023-2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.impl.jdk11;

import co.bitshifted.reflex.core.ReflexClientConfiguration;
import co.bitshifted.reflex.core.exception.HttpClientException;
import co.bitshifted.reflex.core.exception.HttpStatusException;
import co.bitshifted.reflex.core.http.RFXHttpHeaders;
import co.bitshifted.reflex.core.http.RFXHttpResponse;
import co.bitshifted.reflex.core.http.RFXHttpStatus;
import co.bitshifted.reflex.core.serialize.BodySerializer;
import co.bitshifted.reflex.core.ReflexClient;
import co.bitshifted.reflex.core.http.RFXHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static co.bitshifted.reflex.core.Reflex.*;

public class JdkReflexClient implements ReflexClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdkReflexClient.class);

    private final HttpClient httpClient;
    private BodySerializer dataSerializer;

    public JdkReflexClient() {
        this.httpClient =  HttpClient.newHttpClient();
    }

    public JdkReflexClient(ReflexClientConfiguration config) {
        var jdk11Config = Jdk11ConfigConverter.fromConfig(config);
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(jdk11Config.connectTimeout())
                .followRedirects(jdk11Config.redirectPolicy())
                .build();
    }


    @Override
    public <T> RFXHttpResponse sendHttpRequest(RFXHttpRequest<T> request) throws HttpClientException, HttpStatusException {
        var publisher = getRequestBodyPublisher(request);
        var jdkHttpRequest = HttpRequest.newBuilder(request.uri()).method(request.method().name(), publisher).build();
        try {
            var response = httpClient.send(jdkHttpRequest, HttpResponse.BodyHandlers.ofInputStream());
            if(response.statusCode() >= RFXHttpStatus.BAD_REQUEST.code()) {
                throw new HttpStatusException("HTTP error status code: " + response.statusCode());
            }
            var contentType = response.headers().firstValue(RFXHttpHeaders.CONTENT_TYPE);
            Optional<BodySerializer> bodySerializer;
            if(contentType.isPresent()) {
                LOGGER.debug("Response content type: {}", contentType.get());
                bodySerializer = context().getSerializerFor(contentType.get());
            } else {
                LOGGER.debug("No Content-Type header found in response");
                bodySerializer = Optional.empty();
            }
            if(bodySerializer.isPresent()) {
                LOGGER.debug("Found response body serializer {}", bodySerializer.get());
            }

            return new RFXHttpResponse(RFXHttpStatus.findByCode(response.statusCode()), Optional.of(response.body()), bodySerializer, Optional.of(new RFXHttpHeaders()));
        } catch (IOException | InterruptedException ex) {
            throw  new HttpClientException(ex);
        }

    }

    private <T> HttpRequest.BodyPublisher getRequestBodyPublisher(RFXHttpRequest<T> request) throws HttpClientException {
        var publisher = HttpRequest.BodyPublishers.noBody();
        if (request.body() != null && request.body().isPresent()) {
            var contentType = request.headers().get().getHeaderValue(RFXHttpHeaders.CONTENT_TYPE).orElseThrow(() -> new HttpClientException("Request content type not specified"));
            var bodySerializer = context().getSerializerFor(contentType.get(0)).orElseThrow(() -> new HttpClientException("No body serializer found for content type " + contentType.get(0)));
            publisher = HttpRequest.BodyPublishers.ofInputStream(() -> bodySerializer.objectToStream(request.body()));
        }
        return publisher;
    }

}
