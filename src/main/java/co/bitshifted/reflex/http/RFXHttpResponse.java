/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.http;

import co.bitshifted.reflex.serialize.BodySerializer;

import java.util.Optional;

public record RFXHttpResponse(RFXHttpStatus status, Optional<String> body, Optional<BodySerializer> bodySerializer, Optional<RFXHttpHeaders> headers) {

    public <T> T bodyToValue(Class<T> dataType) {
        return bodySerializer.get().stringToObject(body.get(), dataType);
    }


}
