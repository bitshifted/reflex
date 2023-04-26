/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.http;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class RFXMimeTypesTest {

  @Test
  void mimeTypeFromStringTest() {
    var result = RFXMimeTypes.fromString("image/jpeg");
    assertEquals("image", result.type());
    assertEquals("jpeg", result.subtype());

    result = RFXMimeTypes.fromString("application/atom+xml");
    assertEquals("application", result.type());
    assertEquals("atom", result.subtype());
    assertEquals("xml", result.suffix());

    result = RFXMimeTypes.fromString("application/EDI-X12");
    assertEquals("application", result.type());
    assertEquals("EDI-X12", result.subtype());

    result =
        RFXMimeTypes.fromString(
            "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    assertEquals("application", result.type());
    assertEquals("vnd", result.tree());
    assertEquals("openxmlformats-officedocument.presentationml.presentation", result.subtype());

    result = RFXMimeTypes.fromString("application/json; indent=4");
    assertEquals("application", result.type());
    assertEquals("json", result.subtype());
    assertEquals("indent=4", result.param());
  }
}
