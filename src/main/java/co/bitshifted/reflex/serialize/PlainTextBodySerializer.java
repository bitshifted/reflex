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

import co.bitshifted.reflex.http.RFXMimeType;
import co.bitshifted.reflex.http.RFXMimeTypes;

import java.util.Set;

public class PlainTextBodySerializer  implements BodySerializer{
    @Override
    public Set<RFXMimeType> supportedMimeTypes() {
        return Set.of(RFXMimeTypes.TEXT_PLAIN);
    }

    @Override
    public <T> String objectToString(T object) {
        return object.toString();
    }

    @Override
    public <T> T stringToObject(String input, Class<T> type) {
        throw new UnsupportedOperationException("Plain text can not be converted to object");
    }
}
