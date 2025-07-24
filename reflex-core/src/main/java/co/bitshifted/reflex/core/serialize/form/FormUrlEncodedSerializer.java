/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.serialize.form;

import co.bitshifted.reflex.core.exception.BodySerializationException;
import co.bitshifted.reflex.core.http.RFXMimeType;
import co.bitshifted.reflex.core.http.RFXMimeTypes;
import co.bitshifted.reflex.core.serialize.BodySerializer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

public class FormUrlEncodedSerializer implements BodySerializer {
  @Override
  public Set<RFXMimeType> supportedMimeTypes() {
    return Set.of(RFXMimeTypes.FORM_URLENCODED);
  }

  @Override
  public <T> InputStream objectToStream(T object) {

    if (object instanceof Map<?, ?> m) {
      return mapToBody(m);
    } else if (object instanceof FormDataConverter conv) {
      return mapToBody(conv.toFormData());
    }
    return reflectionToBody(object);
  }

  @Override
  public <T> T streamToObject(InputStream input, Class<T> type) {
    return streamToObject(input, type, -1);
  }

  @Override
  public <T> T streamToObject(InputStream input, Class<T> type, long contentLength) {
    throw new UnsupportedOperationException("Not supported");
  }

  private InputStream mapToBody(Map<?, ?> map) {
    var sb = new StringBuilder();
    map.entrySet()
        .forEach(
            entry -> {
              if (entry.getValue() != null) {
                sb.append("&")
                    .append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
              }
            });
    var result = sb.toString();
    if (result.length() > 0 && result.startsWith("&")) {
      result = result.substring(1);
    }
    return new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
  }

  private InputStream reflectionToBody(Object object) {
    var sb = new StringBuilder();
    try {
      var fields = object.getClass().getDeclaredFields();
      for (Field field : fields) {
        field.setAccessible(true);
        if (field.get(object) != null) {
          sb.append("&")
              .append(field.getName())
              .append("=")
              .append(URLEncoder.encode(field.get(object).toString(), StandardCharsets.UTF_8));
        }
      }
      var result = sb.toString();
      if (result.length() > 0 && result.startsWith("&")) {
        result = result.substring(1);
      }
      return new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));

    } catch (Exception ex) {
      throw new BodySerializationException(ex);
    }
  }
}
