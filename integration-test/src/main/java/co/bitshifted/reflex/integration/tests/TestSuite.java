/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.integration.tests;

import co.bitshifted.reflex.integration.tests.json.GsonJsonTestCase;
import co.bitshifted.reflex.integration.tests.json.JacksonJsonTestCase;
import co.bitshifted.reflex.integration.tests.xml.JacksonXmlTestCase;

public enum TestSuite {
  PLAIN_TEXT(PlainTextTestCase.class.getName()),
  JSON_JACKSON(JacksonJsonTestCase.class.getName()),
  JSON_GSON(GsonJsonTestCase.class.getName()),
  XML_JACKSON(JacksonXmlTestCase.class.getName());

  private String className;

  TestSuite(String className) {
    this.className = className;
  }

  public TestCasePackage getTestCasePackage() throws Exception {
    var clazz = Class.forName(className);
    return (TestCasePackage) clazz.getDeclaredConstructor().newInstance();
  }
}
