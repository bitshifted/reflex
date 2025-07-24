/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.impl;

import co.bitshifted.reflex.core.serialize.BodySerializer;
import co.bitshifted.reflex.core.serialize.PlainTextBodySerializer;
import co.bitshifted.reflex.core.serialize.form.FormUrlEncodedSerializer;
import co.bitshifted.reflex.core.serialize.json.GsonBodySerializer;
import co.bitshifted.reflex.core.serialize.json.JacksonJsonBodySerializer;
import co.bitshifted.reflex.core.serialize.xml.JacksonXmlBodySerializer;
import co.bitshifted.reflex.core.serialize.xml.JaxbXmlBodySerializer;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BodySerializerLoader {

  private static final Logger LOGGER = LoggerFactory.getLogger(BodySerializerLoader.class);

  public static enum SupportedBodySerializers {
    JACKSON_JSON_SERIALIZER("com.fasterxml.jackson.databind.ObjectMapper"),
    GSON_JSON_SERIALIZER("com.google.gson.Gson"),
    JACKSON_XML_SERIALIZER("com.fasterxml.jackson.dataformat.xml.XmlMapper"),
    JAXB_XML_SERIALIZER("jakarta.xml.bind.JAXBContext");

    private String className;

    SupportedBodySerializers(String className) {
      this.className = className;
    }

    public String getClassName() {
      return className;
    }
  }

  private BodySerializerLoader() {}

  public static List<BodySerializer> loadBodySerializers() {
    var list = new ArrayList<BodySerializer>();
    list.add(new PlainTextBodySerializer());
    list.add(new FormUrlEncodedSerializer());
    for (SupportedBodySerializers ser : SupportedBodySerializers.values()) {
      switch (ser) {
        case JACKSON_JSON_SERIALIZER -> {
          if (isAvailable(ser)) {
            list.add(new JacksonJsonBodySerializer());
          }
        }
        case GSON_JSON_SERIALIZER -> {
          if (isAvailable(ser)) {
            list.add(new GsonBodySerializer());
          }
        }
        case JACKSON_XML_SERIALIZER -> {
          if (isAvailable(ser)) {
            list.add(new JacksonXmlBodySerializer());
          }
        }
        case JAXB_XML_SERIALIZER -> {
          if (isAvailable(ser)) {
            list.add(new JaxbXmlBodySerializer());
          }
        }
      }
    }
    return list;
  }

  private static boolean isAvailable(SupportedBodySerializers serializer) {
    LOGGER.debug("Looking up serializer with class {}", serializer.className);
    try {
      Class.forName(serializer.className);
      LOGGER.debug("Class {} found", serializer.className);
      return true;
    } catch (ClassNotFoundException ex) {
      return false;
    }
  }
}
