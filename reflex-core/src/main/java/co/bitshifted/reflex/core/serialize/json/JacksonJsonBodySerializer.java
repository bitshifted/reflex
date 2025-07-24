/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.serialize.json;

import co.bitshifted.reflex.core.exception.BodySerializationException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Consumer;

public class JacksonJsonBodySerializer implements JsonBodySerializer {

  private final ObjectMapper mapper;

  public JacksonJsonBodySerializer() {
    this.mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // Ignore null values when writing json.
    mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  public JacksonJsonBodySerializer(Consumer<ObjectMapper> customizer) {
    this.mapper = new ObjectMapper();
    customizer.accept(this.mapper);
  }

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
    } catch (Exception ex) {
      throw new BodySerializationException(ex);
    }
  }
}
