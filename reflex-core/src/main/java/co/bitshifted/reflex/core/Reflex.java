/*
 *
 *  * Copyright (c) 2023-2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core;

import co.bitshifted.reflex.core.config.ReflexContext;
import java.util.HashMap;
import java.util.Map;

public class Reflex {
  public static final String DEFAULT_CONTEXT = "default";

  private static final Reflex INSTANCE;

  static {
    INSTANCE = new Reflex();
  }

  private final Map<String, ReflexContext> contextMap;

  private Reflex() {
    this.contextMap = new HashMap<>();
    this.contextMap.put(DEFAULT_CONTEXT, new ReflexContext());
  }

  public static ReflexContext context() {
    return INSTANCE.contextMap.get(DEFAULT_CONTEXT);
  }

  public static ReflexContext context(String contextName) {
    return INSTANCE.contextMap.computeIfAbsent(contextName, k -> new ReflexContext());
  }

  public static ReflexClient client() {
    return INSTANCE.contextMap.get(DEFAULT_CONTEXT).client();
  }

  public static ReflexClient client(String contextName) {
    ReflexContext context = INSTANCE.contextMap.get(contextName);
    if (context == null) {
      throw new IllegalArgumentException(
          "Context '" + contextName + "' does not exist. Please configure it first.");
    }
    return context.client();
  }
}
