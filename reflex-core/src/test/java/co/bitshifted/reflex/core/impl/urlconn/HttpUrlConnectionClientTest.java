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

import static co.bitshifted.reflex.core.Reflex.context;
import static co.bitshifted.reflex.core.http.RFXHttpMethod.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

import co.bitshifted.reflex.core.exception.HttpStatusException;
import co.bitshifted.reflex.core.http.*;
import co.bitshifted.reflex.core.serialize.PlainTextBodySerializer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.net.URI;
import org.junit.jupiter.api.Test;

@WireMockTest(httpPort = 9020)
public class HttpUrlConnectionClientTest {

  @Test
  void basicGetRequestPlaintextSuccess() throws Exception {
    stubFor(
        get("/test/endpoint")
            .willReturn(ok("test body").withHeader(RFXHttpHeaders.CONTENT_TYPE, "text/plain")));
    context().registerBodySerializer(RFXMimeTypes.TEXT_PLAIN, new PlainTextBodySerializer());
    var client = new HttpUrlConnectionClient();
    var headers = new RFXHttpHeaders();
    headers.setHeader(RFXHttpHeaders.ACCEPT, RFXMimeTypes.TEXT_PLAIN.toMimeTypeString());
    headers.setHeader(RFXHttpHeaders.ACCEPT_LANGUAGE, "rn-US");
    var request =
        RFXHttpRequestBuilder.newBuilder()
            .method(GET)
            .requestUri(new URI("http://localhost:9020/test/endpoint"))
            .build();
    var response = client.sendHttpRequest(request);
    assertNotNull(response);
    assertNotNull(response.body());
    var responseBody = response.bodyToValue(String.class);
    assertEquals("test body", responseBody);
  }

  @Test
  void basicPostRequestSuccess() throws Exception {
    stubFor(post("/test/post").willReturn(noContent()));
    context().registerBodySerializer(RFXMimeTypes.TEXT_PLAIN, new PlainTextBodySerializer());
    var client = new HttpUrlConnectionClient();
    //        var headers = new RFXHttpHeaders();
    //        headers.setHeader(RFXHttpHeaders.CONTENT_TYPE, RFXMimeTypes.TEXT_PLAIN.value());
    var request =
        RFXHttpRequestBuilder.newBuilder("body")
            .method(POST)
            .requestUri(new URI("http://localhost:9020/test/post"))
            .header(RFXHttpHeaders.CONTENT_TYPE, RFXMimeTypes.TEXT_PLAIN.toMimeTypeString())
            .build();
    var response = client.sendHttpRequest(request);
    assertNotNull(response);
    assertEquals(RFXHttpStatus.NO_CONTENT, response.status());
  }

  @Test
  void getRequestFailsWhenErrorStatusFromServer() throws Exception {
    stubFor(get("/test/failure").willReturn(notFound()));
    var client = new HttpUrlConnectionClient();
    var request =
        RFXHttpRequestBuilder.newBuilder()
            .method(GET)
            .requestUri(new URI("http://localhost:9020/test/failure"))
            .build();
    assertThrows(HttpStatusException.class, () -> client.sendHttpRequest(request));
  }
}
