/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.ssl;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrustAllTrustManager implements X509TrustManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(TrustAllTrustManager.class);

  @Override
  public void checkClientTrusted(X509Certificate[] chain, String authType) {
    // Trust all client certificates
  }

  @Override
  public void checkServerTrusted(X509Certificate[] chain, String authType) {
    // Trust all server certificates
    LOGGER.debug("Trusting server certificate:{} ", chain[0].getSubjectDN());
  }

  @Override
  public X509Certificate[] getAcceptedIssuers() {
    return null;
  }
}
