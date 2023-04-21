/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.http;

import co.bitshifted.reflex.core.model.Person;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class RFXHttpRequestBuilderTest {

    @Test
    void createValidHttpRequestWithBody() throws Exception {
        var person = new Person();
        person.setFirstName("john");
        person.setLastName("Smith");
        var request = RFXHttpRequestBuilder.newBuilder(person)
                .method(RFXHttpMethod.GET)
                .requestUri(new URI("http://localhost:8080"))
                .header(RFXHttpHeaders.ACCEPT, "application/json", "application/xml")
                .build();
        assertTrue(request.body().isPresent());
        var body = request.body().get();
        assertNotNull(body);
        assertEquals(person.getFirstName(), body.getFirstName());
        assertEquals(person.getLastName(), body.getLastName());
        var headers = request.headers();
        assertTrue(headers.isPresent());
        var headerValues = headers.get().getHeaderValue(RFXHttpHeaders.ACCEPT);
        assertTrue(headerValues.isPresent());
        assertEquals(2, headerValues.get().size());
        assertTrue(headerValues.get().contains("application/json"));
        assertTrue(headerValues.get().contains("application/xml"));
    }

    @Test
    void createValidHttpRequestWithNoBodyAndNoHeaders() throws Exception {
        var request = RFXHttpRequestBuilder.newBuilder()
                .method(RFXHttpMethod.GET)
                .requestUri(new URI("http://localhost:9000"))
                .build();
        assertTrue(request.body().isEmpty());
        var headers = request.headers();
        assertTrue(headers.isEmpty());
    }

    @Test
    void createRequestWithInvalidArgumentsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> RFXHttpRequestBuilder.newBuilder().build());
    }
}