/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.integration;

import co.bitshifted.reflex.Reflex;
import co.bitshifted.reflex.http.RFXHttpMethod;
import co.bitshifted.reflex.http.RFXHttpRequest;
import co.bitshifted.reflex.http.RFXHttpStatus;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

public class ReflexTester {

    public static void main(String... args) throws Exception {
        var request = new RFXHttpRequest<>(RFXHttpMethod.GET, new URI("http://localhost:9000/v1/text"), Set.of(RFXHttpStatus.OK), Optional.empty(), Optional.empty());
        var response = Reflex.client().sendHttpRequest(request);
        System.out.println("Response: " + response.bodyToValue(String.class));
    }
}
