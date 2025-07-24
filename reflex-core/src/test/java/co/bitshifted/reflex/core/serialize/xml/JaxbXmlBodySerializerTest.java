/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.serialize.xml;

import static org.junit.jupiter.api.Assertions.*;

import co.bitshifted.reflex.core.exception.BodySerializationException;
import co.bitshifted.reflex.core.model.Person;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class JaxbXmlBodySerializerTest {

  private static final String PERSON_XML = "/xml/person.xml";

  private static final String INVALID_XML =
      """
                  <person>
                      <firstName>John</firstName>
                  """;

  @Test
  void streamToObjectReturnsCorrectValue() {
    var serializer = new JaxbXmlBodySerializer();
    var in = getClass().getResourceAsStream(PERSON_XML);
    var result = serializer.streamToObject(in, Person.class);
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    assertEquals("Smith", result.getLastName());
    assertEquals("Oslo", result.getAddress().getCity());
  }

  @Test
  void invalidBodyToXmlThrowsException() {
    var serializer = new JaxbXmlBodySerializer();
    var in = new ByteArrayInputStream(INVALID_XML.getBytes(StandardCharsets.UTF_8));
    assertThrows(
        BodySerializationException.class, () -> serializer.streamToObject(in, Person.class));
  }

  @Test
  void objectToBodySuccess() {
    var serializer = new JaxbXmlBodySerializer();
    var person = new Person();
    person.setFirstName("John");
    person.setLastName("Doe");
    var result = serializer.objectToStream(person);
    assertNotNull(result);
  }

  @Test
  void customSerializerSuccess() {
    var unmarshalTestListener = new UnmarshalTestListener();
    var marshalTestListener = new MarshalTestListener();
    var serializer =
        new JaxbXmlBodySerializer(
            marshaller -> {
              marshaller.setListener(marshalTestListener);
            },
            unmarshaller -> {
              unmarshaller.setListener(unmarshalTestListener);
            });

    var in = getClass().getResourceAsStream(PERSON_XML);
    var result = serializer.streamToObject(in, Person.class);
    assertNotNull(result);
    assertEquals(4, unmarshalTestListener.count);

    var out = serializer.objectToStream(result);
    assertNotNull(out);
    assertEquals(6, marshalTestListener.count);
  }

  private class UnmarshalTestListener extends Unmarshaller.Listener {

    private int count = 0;

    @Override
    public void beforeUnmarshal(Object target, Object parent) {
      super.beforeUnmarshal(target, parent);
      count++;
    }

    @Override
    public void afterUnmarshal(Object target, Object parent) {
      super.afterUnmarshal(target, parent);
      count++;
    }
  }

  private class MarshalTestListener extends Marshaller.Listener {

    private int count = 0;

    @Override
    public void beforeMarshal(Object source) {
      super.beforeMarshal(source);
      count++;
    }

    @Override
    public void afterMarshal(Object source) {
      super.afterMarshal(source);
      count++;
    }
  }
}
