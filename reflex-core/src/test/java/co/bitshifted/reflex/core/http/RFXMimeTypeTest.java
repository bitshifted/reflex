/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.http;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class RFXMimeTypeTest {

  @Test
  void completeMimeTypeTestReturnsCorrectValue() {
    var mime = new RFXMimeType("image", "sub-type", "vnd", "extra", "charset=UTF-8");
    var result = mime.toMimeTypeString();
    var expected = "image/vnd.sub-type+extra;charset=UTF-8";
    assertEquals(expected, result);
  }

  @Test
  void partialMimeTypeValueReturnsCorrectValue() {
    var mime = new RFXMimeType("image", "png", null, "", " ");
    var result = mime.toMimeTypeString();
    assertEquals("image/png", result);
  }

  @Test
  void missingMimeTypePartsReturnError() {
    var th =
        assertThrows(
            NullPointerException.class,
            () -> {
              new RFXMimeType(null, "subtype", null, null, null);
            });
  }
}
