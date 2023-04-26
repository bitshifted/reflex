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

import co.bitshifted.reflex.core.exception.BodySerializationException;
import co.bitshifted.reflex.core.serialize.BodySerializer;
import java.io.InputStream;
import java.util.Optional;

public record RFXHttpResponse(
    RFXHttpStatus status,
    Optional<InputStream> body,
    Optional<BodySerializer> bodySerializer,
    Optional<RFXHttpHeaders> headers) {

  public <T> T bodyToValue(Class<T> dataType) {
    if (body.isEmpty()) {
      throw new BodySerializationException("Response body not present");
    }
    return bodySerializer.get().streamToObject(body.get(), dataType);
  }
}
