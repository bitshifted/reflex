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

public class MimeTypeBuilderTest {

    @Test
    public void completeBuilderSuccessTest() {
        var type = MimeTypeBuilder.newBuilder().withType("application")
                .withSubtype("json")
                .withTree("vnd")
                .withSuffix("ld")
                .withParameter("param=value")
                .build();
        var expect = "application/vnd.json+ld;param=value";
        Assertions.assertEquals(expect, type.value());
    }
}
