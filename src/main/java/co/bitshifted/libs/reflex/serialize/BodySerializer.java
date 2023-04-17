/*
 *
 *  * Copyright (c) 2023-2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.libs.reflex.serialize;

import co.bitshifted.libs.reflex.http.MimeType;

import java.util.Set;

public interface BodySerializer {

    Set<MimeType> supportedMimeTypes();
    <T> String objectToString(T object);
    <T> T stringToObject(String input, Class<T> type);
}
