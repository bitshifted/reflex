/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.serialize.xml;

import co.bitshifted.reflex.core.exception.BodySerializationException;
import co.bitshifted.reflex.core.model.Person;
import co.bitshifted.reflex.core.serialize.json.JacksonJsonBodySerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class JacksonXmlSerializerTest {

    private static final String PERSON_XML = "/xml/person.xml";
    private static final String INVALID_XML = """
            <person>
                <firstName>John</firstName>
            """;

    @Test
    void streamToObjectReturnsCorrectValue() {
        var serializer = new JacksonXmlBodySerializer();
        var in = getClass().getResourceAsStream(PERSON_XML);
        var result = serializer.streamToObject(in, Person.class);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("Oslo", result.getAddress().getCity());
    }

    @Test
    void invalidBodyToXmlThrowsException() {
        var serializer = new JacksonXmlBodySerializer();
        var in = new ByteArrayInputStream(INVALID_XML.getBytes(StandardCharsets.UTF_8));
        assertThrows(
                BodySerializationException.class, () -> serializer.streamToObject(in, Person.class));
    }



    @Test
    void objectToBodySuccess() {
        var serializer = new JacksonXmlBodySerializer();
        var person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        var result = serializer.objectToStream(person);
        assertNotNull(result);
    }

    @Test
    void customSerializerSuccess() {
        var serializer = new JacksonXmlBodySerializer(() -> {
            var mapper = new XmlMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            return mapper;
        });

        var in = getClass().getResourceAsStream(PERSON_XML);
        assertThrows(
                BodySerializationException.class, () -> serializer.streamToObject(in, Person.class));
    }
}
