/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.serialize;

import co.bitshifted.reflex.core.exception.BodySerializationException;
import co.bitshifted.reflex.core.model.Person;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class JacksonJsonBodySerializerTest {

    private static final String PERSON_JSON = "/json/person.json";
    private static final String INVALID_JSON = """
            { "firstName" : "John}
            """;

    @Test
    void streamToObjectReturnsCorrectValue()  {
        var serializer = new JacksonJsonBodySerializer();
        var in = getClass().getResourceAsStream(PERSON_JSON);
        var result = serializer.streamToObject(in, Person.class);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("New York", result.getAddress().getCity());
    }

    @Test
    void invalidBodyToJsonThrowsException() {
        var serializer = new JacksonJsonBodySerializer();
        var in = new ByteArrayInputStream(INVALID_JSON.getBytes(StandardCharsets.UTF_8));
        assertThrows(BodySerializationException.class, () -> serializer.streamToObject(in, Person.class));
    }

    @Test
    void objectToBodySuccess() {
        var serializer = new JacksonJsonBodySerializer();
        var person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        var result = serializer.objectToStream(person);
        assertNotNull(result);
    }

    @Test
    void customSerializerSuccess() {
        var serializer = new JacksonJsonBodySerializer((mapper) -> {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        });
        var in = getClass().getResourceAsStream(PERSON_JSON);
        assertThrows(BodySerializationException.class, () -> serializer.streamToObject(in, Person.class));
    }
}
