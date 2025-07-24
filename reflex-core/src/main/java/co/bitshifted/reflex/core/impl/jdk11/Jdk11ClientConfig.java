/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.impl.jdk11;

import java.net.http.HttpClient;
import java.time.Duration;

public record Jdk11ClientConfig(
    Duration connectTimeout, HttpClient.Redirect redirectPolicy, HttpClient.Version httpVersion) {}
