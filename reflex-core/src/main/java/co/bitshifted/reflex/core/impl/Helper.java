/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.impl;

import co.bitshifted.reflex.core.Reflex;
import co.bitshifted.reflex.core.exception.HttpClientException;
import co.bitshifted.reflex.core.http.RFXHttpRequest;
import java.net.URI;
import java.net.URISyntaxException;

public final class Helper {

  private Helper() {}

  public static <T> URI calculateUri(RFXHttpRequest<T> request) throws HttpClientException {
    var optionalUri = request.uri();
    if (optionalUri.isPresent()) {
      return optionalUri.get();
    } else {
      URI baseUri = Reflex.context().configuration().baseUri();
      if (baseUri == null) {
        throw new HttpClientException("Base URL is not set");
      }
      var path =
          request.path().orElseThrow(() -> new HttpClientException("Request path is missing"));
      try {
        return new URI(baseUri + path);
      } catch (URISyntaxException ex) {
        throw new HttpClientException("Invalid request path: " + path);
      }
    }
  }
}
