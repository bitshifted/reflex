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

import static org.junit.jupiter.api.Assertions.*;

import co.bitshifted.reflex.core.config.ReflexClientConfiguration;
import co.bitshifted.reflex.core.impl.jdk11.JdkReflexClient;
import org.junit.jupiter.api.Test;

public class HttpClientLoaderTest {

  @Test
  void loadDefaultHttpClientSuccess() {
    var result = HttpClientLoader.loadDefaultClient();
    assertTrue(result.isPresent());
    var client = result.get();
    assertTrue(client instanceof JdkReflexClient);
  }

  @Test
  void loadDefaultClientWithConfigSuccess() {
    var config = new ReflexClientConfiguration();
    var result = HttpClientLoader.loadDefaultClient(config);
    assertTrue(result.isPresent());
    var client = result.get();
    assertTrue(client instanceof JdkReflexClient);
  }
}
