/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.impl;

import co.bitshifted.reflex.core.Reflex;
import co.bitshifted.reflex.core.exception.HttpClientException;
import co.bitshifted.reflex.core.http.RFXHttpRequest;

import java.net.URI;
import java.net.URISyntaxException;

public final class Helper {

    private Helper() {

    }

    public static <T> URI calculateUri(RFXHttpRequest<T> request) throws HttpClientException {
        if(request.uri().isPresent()) {
            return request.uri().get();
        } else {
            URI baseUri = Reflex.context().configuration().baseUri();
            if(baseUri == null) {
                throw new HttpClientException("Base URL is not set");
            }
            try {
                return new URI(baseUri.toString() + request.path().get());
            } catch(URISyntaxException ex) {
                throw new HttpClientException("Invalid request path: " + request.path().get());
            }

        }
    }
}
