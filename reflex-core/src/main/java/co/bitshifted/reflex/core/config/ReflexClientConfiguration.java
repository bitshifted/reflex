/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class ReflexClientConfiguration {
  private Duration connectTimeout;
  private Duration readTimeout;
  private RFXRedirectPolicy redirectPolicy;
  private URI baseUri;
  private Map<String, String> commonHeaders;
  private HttpVersion httpVersion;
  private boolean disableSllCertVerification;

  public ReflexClientConfiguration() {
    this.connectTimeout = Duration.of(1, ChronoUnit.SECONDS);
    this.readTimeout = Duration.of(1, ChronoUnit.SECONDS);
    this.redirectPolicy = RFXRedirectPolicy.NORMAL;
    this.commonHeaders = new HashMap<>();
    this.httpVersion = HttpVersion.HTTP_1_1;
    this.disableSllCertVerification = false;
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
    } catch (URISyntaxException ex) {
      throw new RuntimeException(ex);
    }
    return this;
  }

  public Map<String, String> commonHeaders() {
    return commonHeaders;
  }

  public ReflexClientConfiguration commonHeaders(Map<String, String> headers) {
    this.commonHeaders.putAll(headers);
    return this;
  }

  public ReflexClientConfiguration commonHeader(String name, String value) {
    this.commonHeaders.put(name, value);
    return this;
  }

  public ReflexClientConfiguration httpVersion(HttpVersion version) {
    this.httpVersion = version;
    return this;
  }

  public HttpVersion httpVersion() {
    return httpVersion;
  }

  public ReflexClientConfiguration disableSslCertVerification(boolean disable) {
    this.disableSllCertVerification = disable;
    return this;
  }

  public boolean disableSslCertVerification() {
    return disableSllCertVerification;
  }
}
