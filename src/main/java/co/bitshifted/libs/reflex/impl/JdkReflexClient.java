/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.libs.reflex.impl;

import co.bitshifted.libs.reflex.Reflex;
import co.bitshifted.libs.reflex.exception.HttpClientException;
import co.bitshifted.libs.reflex.http.RFXHttpHeaders;
import co.bitshifted.libs.reflex.http.RFXHttpResponse;
import co.bitshifted.libs.reflex.http.RFXHttpStatus;
import co.bitshifted.libs.reflex.serialize.BodySerializer;
import co.bitshifted.libs.reflex.ReflexClient;
import co.bitshifted.libs.reflex.http.RFXHttpRequest;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static co.bitshifted.libs.reflex.Reflex.*;

public class JdkReflexClient implements ReflexClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private BodySerializer dataSerializer;


    @Override
    public <T> RFXHttpResponse sendHttpRequest(RFXHttpRequest<T> request) throws HttpClientException {
        var publisher = getRequestBodyPublisher(request);
        var jdkHttpRequest = HttpRequest.newBuilder(request.uri()).method(request.method().name(), publisher).build();
        try {
            var response = httpClient.send(jdkHttpRequest, HttpResponse.BodyHandlers.ofString());
            var contentType = response.headers().firstValue(RFXHttpHeaders.CONTENT_TYPE);
            var bodySerializer = context().getSerializerFor(contentType.get());
            return new RFXHttpResponse(RFXHttpStatus.findByCode(response.statusCode()), Optional.of(response.body()), bodySerializer, Optional.of(new RFXHttpHeaders()));
        } catch (IOException | InterruptedException ex) {
            throw  new HttpClientException(ex);
        }

    }

    private <T> HttpRequest.BodyPublisher getRequestBodyPublisher(RFXHttpRequest<T> request) throws HttpClientException {
        var publisher = HttpRequest.BodyPublishers.noBody();
        if (request.body() != null) {
            var contentType = request.headers().get().getHeaderValue(RFXHttpHeaders.CONTENT_TYPE).orElseThrow(() -> new HttpClientException("Body content type not specified"));
            var bodySerializer = context().getSerializerFor(contentType.get(0)).orElseThrow(() -> new HttpClientException("No body serializer found for content type " + contentType.get(0)));
            publisher = HttpRequest.BodyPublishers.ofString(bodySerializer.objectToString(request.body()));
        }
        return publisher;
    }

}
