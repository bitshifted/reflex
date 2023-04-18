/*
 *
 *  * Copyright (c) 2023-2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex;

import co.bitshifted.reflex.http.RFXMimeType;
import co.bitshifted.reflex.impl.JdkReflexClient;
import co.bitshifted.reflex.serialize.BodySerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReflexContext {

    private ReflexClient defaultClient;
    private final Map<String, BodySerializer> bodySerializers;

    ReflexContext() {
        this.bodySerializers = new HashMap<>();
        this.defaultClient = new JdkReflexClient();
    }

    public ReflexClient getDefaultClient() {
        return defaultClient;
    }

    public void registerBodySerializer(RFXMimeType mimeType, BodySerializer serializer) {
        this.bodySerializers.put(mimeType.value(), serializer);
    }

    public Optional<BodySerializer> getSerializerFor(String mimeType) {
        return Optional.ofNullable(bodySerializers.get(mimeType));
    }
}
