/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core;

import co.bitshifted.reflex.core.exception.HttpClientException;
import co.bitshifted.reflex.core.exception.HttpStatusException;
import co.bitshifted.reflex.core.http.RFXHttpRequest;
import co.bitshifted.reflex.core.http.RFXHttpResponse;
import java.util.concurrent.CompletableFuture;

public interface ReflexClient {

  <T> RFXHttpResponse sendHttpRequest(RFXHttpRequest<T> request)
      throws HttpClientException, HttpStatusException;

  <T> CompletableFuture<RFXHttpResponse> sendHttpRequestAsync(RFXHttpRequest<T> request);
}
