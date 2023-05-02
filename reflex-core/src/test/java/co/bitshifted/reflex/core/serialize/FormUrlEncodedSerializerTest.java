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

import co.bitshifted.reflex.core.model.Person;
import co.bitshifted.reflex.core.serialize.form.FormUrlEncodedSerializer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class FormUrlEncodedSerializerTest {

    @Test
    void serializeMapToBodySuccess() throws Exception {
        var serializer = new FormUrlEncodedSerializer();
        var input = new HashMap<String, Object>();
        input.put("integer", 12);
        input.put("boolean", true);
        input.put("string-space", "string with space");
        input.put("special-char", "string&with%special chars");
        input.put("null-param", null);

        var result = serializer.objectToStream(input);
        assertNotNull(result);
        var content = new String(result.readAllBytes());
        assertEquals("string-space=string+with+space&boolean=true&integer=12&special-char=string%26with%25special+chars", content);
    }

    @Test
    void serializeReflectionToBody() throws Exception {
        var person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith Rowe");
        person.setAge(20);

        var serializer = new FormUrlEncodedSerializer();
        var result = serializer.objectToStream(person);
        assertNotNull(result);
        var content =  new String(result.readAllBytes());
        assertEquals("firstName=John&lastName=Smith+Rowe&age=20", content);
    }

    @Test
    void streamToObjectNotSupported() {
        var serializer = new FormUrlEncodedSerializer();
        assertThrows(UnsupportedOperationException.class, () -> serializer.streamToObject(new ByteArrayInputStream(new byte[1]), String.class));
    }
}
