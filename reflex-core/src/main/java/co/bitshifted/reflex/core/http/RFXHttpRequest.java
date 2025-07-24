/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.http;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public record RFXHttpRequest<T>(
    RFXHttpMethod method,
    Optional<URI> uri,
    Optional<T> body,
    Optional<RFXHttpHeaders> headers,
    Optional<String> path) {
  public RFXHttpRequest {
    Objects.requireNonNull(method);
    Objects.requireNonNull(uri);
    Objects.requireNonNull(body);
    Objects.requireNonNull(headers);
    Objects.requireNonNull(path);
  }
}
