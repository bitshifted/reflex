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

public final class MimeTypes {

    private MimeTypes() {

    }

    public static final MimeType APPLICATION_JSON = new MimeType("application", "json");
    public static  final MimeType APPLICATION_XML = new MimeType("application", "xml");
    public static final MimeType TEXT_PLAIN = new MimeType("text", "plain");
}
