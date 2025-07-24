/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.config;

import static org.junit.jupiter.api.Assertions.*;

import co.bitshifted.reflex.core.http.RFXMimeTypes;
import co.bitshifted.reflex.core.serialize.json.JacksonJsonBodySerializer;
import org.junit.jupiter.api.Test;

public class ReflexContextTest {

  @Test
  void getBodySerializerForExactMatchSuccess() {
    var ctx = new ReflexContext();
    var serializer = new JacksonJsonBodySerializer();
    ctx.registerBodySerializer(RFXMimeTypes.APPLICATION_JSON, serializer);
    var result = ctx.getSerializerFor(RFXMimeTypes.APPLICATION_JSON.toMimeTypeString());
    assertNotNull(result);
    assertTrue(result.isPresent());
    assertEquals(serializer, result.get());
  }

  @Test
  void getBodySerializerPartialMatchSuccess() {
    var ctx = new ReflexContext();
    var serializer = new JacksonJsonBodySerializer();
    ctx.registerBodySerializer(RFXMimeTypes.APPLICATION_JSON, serializer);
    var result = ctx.getSerializerFor("application/json+ld");
    assertNotNull(result);
    assertTrue(result.isPresent());
    assertEquals(serializer, result.get());
  }

  @Test
  void getBodySerializerForUnknownReturnEmpty() {
    var ctx = new ReflexContext();
    var serializer = new JacksonJsonBodySerializer();
    ctx.registerBodySerializer(RFXMimeTypes.APPLICATION_JSON, serializer);
    var result = ctx.getSerializerFor("image/png");
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
