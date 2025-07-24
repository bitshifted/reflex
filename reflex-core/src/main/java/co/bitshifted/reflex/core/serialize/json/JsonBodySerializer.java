/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.serialize.json;

import co.bitshifted.reflex.core.http.RFXMimeType;
import co.bitshifted.reflex.core.http.RFXMimeTypes;
import co.bitshifted.reflex.core.serialize.BodySerializer;
import java.io.InputStream;
import java.util.Set;

public interface JsonBodySerializer extends BodySerializer {

  @Override
  default Set<RFXMimeType> supportedMimeTypes() {
    return Set.of(RFXMimeTypes.APPLICATION_JSON);
  }

  @Override
  default <T> T streamToObject(InputStream input, Class<T> type, long contentLength) {
    return streamToObject(input, type);
  }
}
