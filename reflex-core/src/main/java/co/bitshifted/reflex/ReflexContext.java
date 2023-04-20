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
import co.bitshifted.reflex.impl.BodySerializerLoader;
import co.bitshifted.reflex.impl.HttpClientLoader;
import co.bitshifted.reflex.impl.jdk11.JdkReflexClient;
import co.bitshifted.reflex.serialize.BodySerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReflexContext {

    private ReflexClient defaultClient;
    private final Map<String, BodySerializer> bodySerializers;
    private boolean serializersLoaded = false;

    ReflexContext() {
        this.bodySerializers = new HashMap<>();
    }

    public ReflexClient defaultClient() {
        if(defaultClient == null) {
            defaultClient =HttpClientLoader.loadDefaultClient().get();
        }
        if(!serializersLoaded) {
            BodySerializerLoader.loadBodySerializers().stream().forEach(ser ->
                ser.supportedMimeTypes().stream().forEach(s -> bodySerializers.put(s.value(), ser)));
        }
        return defaultClient;
    }

    public void configureDefaultClient(ReflexClientConfiguration config) {
        synchronized (defaultClient) {
            defaultClient =HttpClientLoader.loadDefaultClient(config).get();
        }
    }

    public void registerBodySerializer(RFXMimeType mimeType, BodySerializer serializer) {
        this.bodySerializers.put(mimeType.value(), serializer);
    }

    public Optional<BodySerializer> getSerializerFor(String mimeType) {
        return Optional.ofNullable(bodySerializers.get(mimeType));
    }
}
