/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.impl;

import co.bitshifted.reflex.core.serialize.BodySerializer;
import co.bitshifted.reflex.core.serialize.JacksonJsonBodySerializer;
import co.bitshifted.reflex.core.serialize.PlainTextBodySerializer;

import java.util.ArrayList;
import java.util.List;

public class BodySerializerLoader {

    public static enum SupportedBodySerializers {
        JACKSON_JSON_SERIALIZER("com.fasterxml.jackson.databind.ObjectMapper");

        private String className;

        SupportedBodySerializers(String className) {
            this.className = className;
        }

        public String getClassName() {
            return className;
        }
    }

    private BodySerializerLoader() {

    }

    public static List<BodySerializer> loadBodySerializers() {
        var list = new ArrayList<BodySerializer>();
        list.add(new PlainTextBodySerializer());
        for(SupportedBodySerializers ser : SupportedBodySerializers.values()) {
            switch (ser) {
                case JACKSON_JSON_SERIALIZER -> {
                    if(isAvailable(ser)) {
                        list.add(new JacksonJsonBodySerializer());
                    }
                }
            }
        }
        return list;
    }

    private static boolean isAvailable(SupportedBodySerializers serializer) {
        try {
            Class.forName(serializer.className);
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
}
