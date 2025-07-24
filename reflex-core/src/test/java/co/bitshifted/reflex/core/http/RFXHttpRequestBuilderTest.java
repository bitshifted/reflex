/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.http;

import static co.bitshifted.reflex.core.http.RFXHttpRequestBuilder.*;
import static co.bitshifted.reflex.core.http.RFXHttpRequestBuilder.UrlTemplateBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import co.bitshifted.reflex.core.model.Person;
import org.junit.jupiter.api.Test;

public class RFXHttpRequestBuilderTest {

  @Test
  void createValidHttpRequestWithBody() throws Exception {
    var person = new Person();
    person.setFirstName("john");
    person.setLastName("Smith");
    var request =
        newBuilder(person)
            .method(RFXHttpMethod.POST)
            .requestUri("http://localhost:8080")
            .header(RFXHttpHeaders.ACCEPT, "application/json", "application/xml")
            .header(RFXHttpHeaders.CONTENT_TYPE, "application/json")
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
    var request =
        newBuilder().method(RFXHttpMethod.GET).requestUri("http://localhost:9000").build();
    assertTrue(request.body().isEmpty());
    var headers = request.headers();
    assertTrue(headers.isEmpty());
  }

  @Test
  void createRequestWithInvalidArgumentsThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> newBuilder().build());
  }

  @Test
  void createRequestWithUrlTemplateAndQueryParams() {
    var request =
        newBuilder()
            .method(RFXHttpMethod.GET)
            .urlTemplate(
                urlTemplate("/{foo}/{bar}")
                    .pathParam("foo", "foo-value")
                    .pathParam("bar", "bar-value")
                    .queryParam("param1", "param1 value"))
            .build();
    assertEquals("/foo-value/bar-value?param1=param1+value", request.path().get());
  }

  @Test
  void createRequestWithUrlTemplateAndNoQueryParams() {
    var request =
        newBuilder()
            .method(RFXHttpMethod.GET)
            .urlTemplate(
                urlTemplate("/{foo}/{bar}")
                    .pathParam("foo", "foo-value")
                    .pathParam("bar", "bar-value"))
            .build();
    assertEquals("/foo-value/bar-value", request.path().get());
  }

  @Test
  void shouldThrowExceptionWhenBodyPresentAndNoContentType() throws Exception {
    var request = newBuilder("body").method(RFXHttpMethod.POST).requestUri("http://localhost");
    assertThrows(IllegalArgumentException.class, () -> request.build());
  }
}
