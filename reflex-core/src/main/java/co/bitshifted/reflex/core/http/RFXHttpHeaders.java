/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.http;

import java.util.*;

public class RFXHttpHeaders {

  public static final String ACCEPT = "Accept";
  public static final String ACCEPT_LANGUAGE = "Accept-Language";
  public static final String CONTENT_TYPE = "Content-Type";
  public static final String CONTENT_LENGTH = "Content-Length";

  public static final String LOCATION = "Location";

  private final Map<String, List<String>> headersMap;

  public RFXHttpHeaders() {
    this.headersMap = new HashMap<>();
  }

  public void setHeader(String name, String value) {
    if (headersMap.containsKey(name)) {
      headersMap.get(name).add(value);
    } else {
      var list = new ArrayList<String>();
      list.add(value);
      headersMap.put(name, list);
    }
  }

  public Optional<List<String>> getHeaderValue(String name) {
    if (headersMap.containsKey(name)) {
      return Optional.of(headersMap.get(name));
    }
    return Optional.empty();
  }

  public Map<String, List<String>> getAllHeaders() {
    return headersMap;
  }

  public boolean isEmpty() {
    return headersMap.isEmpty();
  }
}
