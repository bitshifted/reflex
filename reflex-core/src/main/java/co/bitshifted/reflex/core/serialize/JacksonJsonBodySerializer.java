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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class JacksonJsonBodySerializer implements JsonBodySerializer {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> InputStream objectToStream(T object) {
        try {
            byte[] dataBytes = mapper.writeValueAsBytes(object);
            return new ByteArrayInputStream(dataBytes);
        } catch (Exception ex) {
            throw new BodySerializationException(ex);
        }
    }

    @Override
    public <T> T streamToObject(InputStream input, Class<T> type) {
        try {
            return mapper.readValue(input, type);
        } catch(Exception ex) {
            throw new BodySerializationException(ex);
        }

    }
}
