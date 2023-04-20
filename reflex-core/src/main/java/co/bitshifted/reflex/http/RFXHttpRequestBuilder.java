/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.http;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Stream;

public final class RFXHttpRequestBuilder<T> {

    private RFXHttpMethod method;
    private URI requestUri;
    private T body;
    private RFXHttpHeaders headers;

    private RFXHttpRequestBuilder() {
        this.headers = new RFXHttpHeaders();
    }

    public static <T> RFXHttpRequestBuilder<T> newBuilder() {
        return new RFXHttpRequestBuilder<>();
    }

    public static <T> RFXHttpRequestBuilder<T> newBuilder(T body) {
        var builder = new RFXHttpRequestBuilder<T>();
        builder.body = body;
        return builder;
    }

    public RFXHttpRequestBuilder<T> method(RFXHttpMethod method) {
        this.method = method;
        return this;
    }

    public RFXHttpRequestBuilder<T> requestUri(URI uri) {
        this.requestUri = uri;
        return this;
    }

    public RFXHttpRequestBuilder<T> header(String name, String... values) {
        Stream.of(values).forEach(v -> this.headers.setHeader(name, v));
        return this;
    }

    public RFXHttpRequest<T> build() {
        if(method == null || requestUri == null) {
            throw new IllegalArgumentException("Method and URI are required");
        }
        var bodyOpt = (body != null) ? Optional.of(body) : Optional.empty();
        var optHeaders = (headers.isEmpty()) ? Optional.empty() : Optional.of(headers);
        return new RFXHttpRequest(method, requestUri, bodyOpt, optHeaders);
    }
}
