/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.impl.jdk11;

import co.bitshifted.reflex.core.config.HttpVersion;
import co.bitshifted.reflex.core.config.RFXRedirectPolicy;
import co.bitshifted.reflex.core.config.ReflexClientConfiguration;
import java.net.http.HttpClient;

public class Jdk11ConfigConverter {

  private Jdk11ConfigConverter() {}

  public static Jdk11ClientConfig fromConfig(ReflexClientConfiguration config) {
    return new Jdk11ClientConfig(
        config.connectTimeout(),
        getRedirectPolicy(config.redirectPolicy()),
        getHttpVersion(config.httpVersion()));
  }

  private static HttpClient.Redirect getRedirectPolicy(RFXRedirectPolicy input) {
    return switch (input) {
      case NEVER -> HttpClient.Redirect.NEVER;
      case ALWAYS -> HttpClient.Redirect.ALWAYS;
      case NORMAL -> HttpClient.Redirect.NORMAL;
    };
  }

  private static HttpClient.Version getHttpVersion(HttpVersion source) {
    return switch (source) {
      case HTTP_1_1 -> HttpClient.Version.HTTP_1_1;
      case HTTP_2_0 -> HttpClient.Version.HTTP_2;
    };
  }
}
