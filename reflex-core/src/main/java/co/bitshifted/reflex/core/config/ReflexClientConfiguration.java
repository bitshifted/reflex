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

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ReflexClientConfiguration {
    private  Duration connectTimeout;
    private Duration readTimeout;
    private RFXRedirectPolicy redirectPolicy;
    private URI baseUri;

    public ReflexClientConfiguration() {
        this.connectTimeout = Duration.of(1, ChronoUnit.SECONDS);
        this.readTimeout = Duration.of(1, ChronoUnit.SECONDS);
        this.redirectPolicy = RFXRedirectPolicy.NORMAL;
    }

    public Duration connectTimeout() {
        return connectTimeout;
    }

    public ReflexClientConfiguration connectTimeout(Duration value) {
        this.connectTimeout = value;
        return this;
    }

    public Duration readTimeout() {
        return readTimeout;
    }

    public ReflexClientConfiguration readTimeout(Duration value) {
        this.readTimeout = value;
        return this;
    }

    public RFXRedirectPolicy redirectPolicy() {
        return redirectPolicy;
    }

    public ReflexClientConfiguration redirectPolicy(RFXRedirectPolicy value) {
        this.redirectPolicy = value;
        return this;
    }

    public URI baseUri() {
        return baseUri;
    }

    public ReflexClientConfiguration baseUri(String uri) {
        try {
            this.baseUri = new URI(uri);
        } catch(URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }
}
