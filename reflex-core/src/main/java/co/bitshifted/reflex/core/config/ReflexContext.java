/*
 *
 *  * Copyright (c) 2023-2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.config;

import co.bitshifted.reflex.core.ReflexClient;
import co.bitshifted.reflex.core.http.RFXMimeType;
import co.bitshifted.reflex.core.impl.BodySerializerLoader;
import co.bitshifted.reflex.core.impl.HttpClientLoader;
import co.bitshifted.reflex.core.serialize.BodySerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReflexContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflexContext.class);

    private ReflexClient defaultClient;
    private final ReflexClientConfiguration defaultClientDonfiguration;
    private final Map<String, BodySerializer> bodySerializers;
    private boolean serializersLoaded = false;

    public ReflexContext() {
        this.bodySerializers = new HashMap<>();
        this.defaultClientDonfiguration = new ReflexClientConfiguration();
    }

    public ReflexClient defaultClient() {
        if(defaultClient == null) {
            defaultClient =HttpClientLoader.loadDefaultClient().get();
        }
        if(!serializersLoaded) {
            BodySerializerLoader.loadBodySerializers().stream().forEach(ser ->
                ser.supportedMimeTypes().stream().forEach(s -> bodySerializers.put(s.value(), ser)));
            LOGGER.debug("Loaded serializers: {}", bodySerializers);
        }
        return defaultClient;
    }

    public ReflexClientConfiguration configuration() {
        synchronized (this) {
            return defaultClientDonfiguration;
        }
    }

    public void registerBodySerializer(RFXMimeType mimeType, BodySerializer serializer) {
        this.bodySerializers.put(mimeType.value(), serializer);
    }

    public Optional<BodySerializer> getSerializerFor(String mimeType) {
        LOGGER.debug("Looking up serializers for MIME type: {}", mimeType);
        return Optional.ofNullable(bodySerializers.get(mimeType));
    }
}
