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
import co.bitshifted.reflex.core.http.RFXMimeType;
import co.bitshifted.reflex.core.http.RFXMimeTypes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class PlainTextBodySerializer  implements BodySerializer{
    @Override
    public Set<RFXMimeType> supportedMimeTypes() {
        return Set.of(RFXMimeTypes.TEXT_PLAIN);
    }

    @Override
    public <T> InputStream objectToStream(T object) {
       var text = object.toString();
       return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public <T> T streamToObject(InputStream input, Class<T> type) {
        if(type == String.class) {
            try {
                return type.getConstructor(byte[].class).newInstance(input.readAllBytes());
            } catch(Exception ex) {
                throw new BodySerializationException(ex);
            }

        }
        throw new UnsupportedOperationException("Only String type is supported");
    }
}
