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

import static co.bitshifted.reflex.core.Reflex.client;
import static co.bitshifted.reflex.core.Reflex.context;
import static co.bitshifted.reflex.core.http.RFXHttpMethod.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

import co.bitshifted.reflex.core.exception.HttpStatusException;
import co.bitshifted.reflex.core.http.*;
import co.bitshifted.reflex.core.serialize.PlainTextBodySerializer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.net.URI;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@WireMockTest(httpPort = 9010)
public class JdkHttpClientImplTest {

  @Test
  void basicGetRequestSuccess() throws Exception {
    stubFor(
        get("/test/endpoint")
            .willReturn(ok("test body").withHeader(RFXHttpHeaders.CONTENT_TYPE, "text/plain")));
    context().registerBodySerializer(RFXMimeTypes.TEXT_PLAIN, new PlainTextBodySerializer());
    var client = new JdkReflexClient();
    var request =
        RFXHttpRequestBuilder.newBuilder()
            .method(GET)
            .requestUri(new URI("http://localhost:9010/test/endpoint"))
            .build();
    var response = client.sendHttpRequest(request);
    assertNotNull(response);
    assertNotNull(response.body());
    var responseBody = response.bodyToValue(String.class);
    assertEquals("test body", responseBody);
  }

  @Test
  @Disabled
  void basicPostRequestWhenResponseStatusOKSuccess() throws Exception {
    stubFor(
        post("/test/post")
            .willReturn(
                ok("response body")
                    .withHeader(
                        RFXHttpHeaders.CONTENT_TYPE, RFXMimeTypes.TEXT_PLAIN.toMimeTypeString())));
    context().registerBodySerializer(RFXMimeTypes.TEXT_PLAIN, new PlainTextBodySerializer());
    var headers = new RFXHttpHeaders();
    headers.setHeader(RFXHttpHeaders.CONTENT_TYPE, RFXMimeTypes.TEXT_PLAIN.toMimeTypeString());
    var client = new JdkReflexClient();
    var request =
        RFXHttpRequestBuilder.newBuilder("content")
            .method(POST)
            .requestUri(new URI("http://localhost:9010/test/post"))
            .build();
    var response = client.sendHttpRequest(request);
    assertNotNull(response);
    assertEquals(RFXHttpStatus.OK, response.status());
    assertEquals("response body", response.bodyToValue(String.class));
  }

  @Test
  void invalidResponseStatusShouldThrowException() throws Exception {
    stubFor(get("/test/wrong-status").willReturn(badRequest()));
    var request =
        RFXHttpRequestBuilder.newBuilder()
            .method(GET)
            .requestUri(new URI("http://localhost:9010/test/wrong-status"))
            .build();
    assertThrows(HttpStatusException.class, () -> client().sendHttpRequest(request));
  }
}
