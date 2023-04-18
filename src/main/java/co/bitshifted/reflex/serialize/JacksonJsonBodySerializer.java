/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.serialize;

import java.io.InputStream;

public class JacksonJsonBodySerializer implements JsonBodySerializer {

    @Override
    public <T> InputStream objectToStream(T object) {
        return null;
    }

    @Override
    public <T> T streamToObject(InputStream input, Class<T> type) {
        return null;
    }
}
