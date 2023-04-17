/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.libs.reflex.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MimeTypeTest {

    @Test
    void completeMimeTypeTestReturnsCorrectValue() {
        var mime = new MimeType("image", "sub-type", "vnd", "extra", "charset=UTF-8");
        var result = mime.value();
        var expected = "image/vnd.sub-type+extra;charset=UTF-8";
        assertEquals(expected, result);
    }

    @Test
    void missingMimeTypePartsReturnError() {
        var th = assertThrows(NullPointerException.class, () -> {new MimeType(null, "subtype", null, null, null);});
    }
}
