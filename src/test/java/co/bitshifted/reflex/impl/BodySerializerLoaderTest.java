/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.impl;

import co.bitshifted.reflex.serialize.JacksonJsonBodySerializer;
import co.bitshifted.reflex.serialize.PlainTextBodySerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BodySerializerLoaderTest {

    @Test
    void defaultSerializerLoadedSuccess() {
        var list = BodySerializerLoader.loadBodySerializers();
        assertTrue(list.stream().filter(s -> s.getClass() == PlainTextBodySerializer.class).findFirst().isPresent());
        assertTrue(list.stream().filter(s -> s.getClass() == JacksonJsonBodySerializer.class).findFirst().isPresent());
    }
}
