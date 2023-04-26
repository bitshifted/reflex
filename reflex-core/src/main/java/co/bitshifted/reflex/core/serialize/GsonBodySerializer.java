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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.function.Function;

public class GsonBodySerializer implements BodySerializer {

  private final Gson gson;

  public GsonBodySerializer() {
    this.gson = new Gson();
  }

  public GsonBodySerializer(Function<GsonBuilder, Gson> customizer) {
    this.gson = customizer.apply(new GsonBuilder());
  }

  @Override
  public Set<RFXMimeType> supportedMimeTypes() {
    return Set.of(RFXMimeTypes.APPLICATION_JSON);
  }

  @Override
  public <T> InputStream objectToStream(T object) {
    var json = gson.toJson(object);
    return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public <T> T streamToObject(InputStream input, Class<T> type) {
    try {
      return gson.fromJson(new InputStreamReader(input), type);
    } catch (Exception ex) {
      throw new BodySerializationException(ex);
    }
  }
}
