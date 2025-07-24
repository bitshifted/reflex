/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.serialize;

import co.bitshifted.reflex.core.http.RFXMimeType;
import java.io.InputStream;
import java.util.Set;

public interface BodySerializer {

  Set<RFXMimeType> supportedMimeTypes();

  <T> InputStream objectToStream(T object);

  <T> T streamToObject(InputStream input, Class<T> type);

  <T> T streamToObject(InputStream input, Class<T> type, long contentLength);
}
