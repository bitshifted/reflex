/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
module co.bitshifted.libs.httpjfx {
  requires static java.net.http;
  requires static com.fasterxml.jackson.databind;
  requires static com.fasterxml.jackson.dataformat.xml;
  requires static com.google.gson;
  requires static jakarta.xml.bind;
  requires org.slf4j;

  exports co.bitshifted.reflex.core.http;
  exports co.bitshifted.reflex.core.config;
  exports co.bitshifted.reflex.core.serialize;
  exports co.bitshifted.reflex.core;
  exports co.bitshifted.reflex.core.serialize.json;
  exports co.bitshifted.reflex.core.serialize.xml;
  exports co.bitshifted.reflex.core.serialize.form;
}
