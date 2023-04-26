/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.impl.urlconn;

import co.bitshifted.reflex.core.config.RFXRedirectPolicy;
import co.bitshifted.reflex.core.config.ReflexClientConfiguration;

public class HttpUrlConnectionConfigConverter {

  public static HttpUrlConnectionClientConfig fromConfig(ReflexClientConfiguration config) {
    boolean doRedirect = config.redirectPolicy() != RFXRedirectPolicy.NEVER;
    return new HttpUrlConnectionClientConfig(
        config.connectTimeout().getSeconds() * 1000,
        config.readTimeout().getSeconds() * 1000,
        doRedirect);
  }
}
