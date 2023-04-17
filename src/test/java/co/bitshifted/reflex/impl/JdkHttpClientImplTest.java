/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.impl;

import co.bitshifted.reflex.Reflex;
import co.bitshifted.reflex.http.HttpMethod;
import co.bitshifted.reflex.http.RFXHttpHeaders;
import co.bitshifted.reflex.http.RFXHttpRequest;
import co.bitshifted.reflex.http.RFXHttpStatus;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Set;

@WireMockTest(httpPort = 9000)
public class JdkHttpClientImplTest {

    @Test
    void basicGetRequestSuccess() throws Exception {
        WireMock.stubFor(WireMock.get("/test/endpoint").willReturn(WireMock.ok("body").withHeader(RFXHttpHeaders.CONTENT_TYPE, "text/plain")));
        var response = Reflex.client().sendHttpRequest(new RFXHttpRequest<>(HttpMethod.GET, new URI("http://localhost:9000/test/endpoint"), Set.of(RFXHttpStatus.OK), null, null));
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.body());
    }
}
