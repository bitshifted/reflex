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

public interface JsonBodySerializer extends BodySerializer {

    @Override
    default Set<RFXMimeType> supportedMimeTypes() {
        return Set.of(RFXMimeTypes.APPLICATION_JSON);
    }
}
