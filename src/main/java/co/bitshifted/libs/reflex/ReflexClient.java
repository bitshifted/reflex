/*
 *
 *  * Copyright (c) 2023-2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.libs.reflex;

import co.bitshifted.libs.reflex.exception.HttpClientException;
import co.bitshifted.libs.reflex.http.RFXHttpResponse;
import co.bitshifted.libs.reflex.http.RFXHttpRequest;

public interface ReflexClient {

    <T> RFXHttpResponse sendHttpRequest(RFXHttpRequest<T> request) throws HttpClientException;
}
