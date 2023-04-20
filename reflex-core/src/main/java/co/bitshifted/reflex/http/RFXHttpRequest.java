/*
 *
 *  * Copyright (c) 2023-2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */

package co.bitshifted.reflex.http;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public record RFXHttpRequest<T>(RFXHttpMethod method, URI uri, Set<RFXHttpStatus> successStatus,
                                Optional<T> body, Optional<RFXHttpHeaders> headers) {
    public RFXHttpRequest{
        Objects.requireNonNull(method);
        Objects.requireNonNull(uri);
        Objects.requireNonNull(successStatus);
        if (successStatus.isEmpty()) {
            throw new IllegalArgumentException("At least one success status is required");
        }
        Objects.requireNonNull(body);
        Objects.requireNonNull(headers);
    }
}
