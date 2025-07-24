/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.http;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

public final class RFXHttpRequestBuilder<T> {

  private RFXHttpMethod method;
  private URI requestUri;
  private T body;
  private RFXHttpHeaders headers;

  private String path;

  private RFXHttpRequestBuilder() {
    this.headers = new RFXHttpHeaders();
  }

  public static <T> RFXHttpRequestBuilder<T> newBuilder() {
    return new RFXHttpRequestBuilder<>();
  }

  public static <T> RFXHttpRequestBuilder<T> newBuilder(T body) {
    var builder = new RFXHttpRequestBuilder<T>();
    builder.body = body;
    return builder;
  }

  public RFXHttpRequestBuilder<T> method(RFXHttpMethod method) {
    this.method = method;
    return this;
  }

  public RFXHttpRequestBuilder<T> requestUri(String uri) {
    this.requestUri = URI.create(uri);
    return this;
  }

  public RFXHttpRequestBuilder<T> header(String name, String... values) {
    Stream.of(values).forEach(v -> this.headers.setHeader(name, v));
    return this;
  }

  public RFXHttpRequestBuilder<T> path(String value) {
    this.path = value;
    return this;
  }

  public RFXHttpRequestBuilder<T> urlTemplate(UrlTemplateBuilder builder) {
    this.path = builder.url();
    return this;
  }

  public RFXHttpRequest<T> build() {
    if (method == null || (requestUri == null && path == null)) {
      throw new IllegalArgumentException("Method and URI are required");
    }
    if (body != null && headers.getHeaderValue(RFXHttpHeaders.CONTENT_TYPE).isEmpty()) {
      throw new IllegalArgumentException(
          "Header Content-Type must be specified when request body is present");
    }
    var bodyOpt = (body != null) ? Optional.of(body) : Optional.empty();
    var optHeaders = (headers.isEmpty()) ? Optional.empty() : Optional.of(headers);
    return new RFXHttpRequest(
        method, Optional.ofNullable(requestUri), bodyOpt, optHeaders, Optional.ofNullable(path));
  }

  public static class UrlTemplateBuilder {

    private final String template;
    private final Map<String, String> pathParams;
    private final Map<String, String> queryParams;

    private UrlTemplateBuilder(String template) {
      this.template = template;
      this.pathParams = new HashMap<>();
      this.queryParams = new HashMap<>();
    }

    public static UrlTemplateBuilder urlTemplate(String template) {
      return new UrlTemplateBuilder(template);
    }

    public UrlTemplateBuilder pathParam(String paramName, String value) {
      pathParams.put(paramName, value);
      return this;
    }

    public UrlTemplateBuilder queryParam(String paramName, String value) {
      queryParams.put(paramName, value);
      return this;
    }

    public String url() {
      String path = template;
      for (Map.Entry<String, String> entry : pathParams.entrySet()) {
        path = path.replace("{" + entry.getKey() + "}", entry.getValue());
      }
      var sb = new StringBuilder(path);
      if (!queryParams.isEmpty()) {
        sb.append("?");
      }
      for (Map.Entry<String, String> entry : queryParams.entrySet()) {
        sb.append(entry.getKey())
            .append("=")
            .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
      }
      return sb.toString();
    }
  }
}
