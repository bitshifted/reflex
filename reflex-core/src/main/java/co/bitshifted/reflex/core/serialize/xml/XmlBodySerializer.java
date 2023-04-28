/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.serialize.xml;

import co.bitshifted.reflex.core.http.RFXMimeType;
import co.bitshifted.reflex.core.http.RFXMimeTypes;
import co.bitshifted.reflex.core.serialize.BodySerializer;
import java.util.Set;

public interface XmlBodySerializer extends BodySerializer {

  @Override
  default Set<RFXMimeType> supportedMimeTypes() {
    return Set.of(RFXMimeTypes.APPLICATION_XML);
  }
}