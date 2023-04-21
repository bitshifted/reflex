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

import co.bitshifted.reflex.core.ReflexClient;
import co.bitshifted.reflex.core.ReflexClientConfiguration;
import co.bitshifted.reflex.core.impl.jdk11.JdkReflexClient;

import java.util.Optional;

/**
 * Creates and initializes HTTP client library based on what is available at classpath (or module path).
 */
public class HttpClientLoader {

    private static enum SupportedHttpClient {
        JDK11_HTTP_CLIENT ("java.net.http.HttpClient"),
        HTTP_URL_CONNECTION ("java.net.HttpURLConnection");

        private String className;
        SupportedHttpClient(String className) {
            this.className = className;
        }
    }

    private HttpClientLoader() {

    }

    public static Optional<ReflexClient> loadDefaultClient() {
        for(SupportedHttpClient client : SupportedHttpClient.values()) {
            switch (client) {
                case JDK11_HTTP_CLIENT -> {
                    if(isClientAvailable(client)) {
                        return Optional.of(new JdkReflexClient());
                    }
                }
            };
        }
        return Optional.empty();
    }

    public static Optional<ReflexClient> loadDefaultClient(ReflexClientConfiguration config) {
        for(SupportedHttpClient client : SupportedHttpClient.values()) {
            switch (client) {
                case JDK11_HTTP_CLIENT -> {
                    if(isClientAvailable(client)) {
                        return Optional.of(new JdkReflexClient(config));
                    }
                }
            };
        }
        return Optional.empty();
    }

    private static boolean isClientAvailable(SupportedHttpClient client) {
        try {
            Class.forName(client.className);
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

}
