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
import co.bitshifted.libs.reflex.http.HttpMethod;
import co.bitshifted.libs.reflex.http.RFXHttpHeaders;
import co.bitshifted.libs.reflex.http.RFXHttpRequest;
import co.bitshifted.libs.reflex.http.RFXHttpStatus;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.hc.client5.http.impl.Wire;
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
