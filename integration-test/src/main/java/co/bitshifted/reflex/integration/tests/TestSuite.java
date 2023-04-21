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

public enum TestSuite {
    JDK11_PLAIN_TEXT (Jdk11ClientPlainTextTestCase.class.getName()),
    JDK11_JSON_JACKSON(Jdk11jacksonJsonTestCase.class.getName());

    private String className;

    TestSuite(String className) {
        this.className = className;
    }


    public TestCasePackage getTestCasePackage() throws Exception {
        var clazz = Class.forName(className);
        return (TestCasePackage)clazz.getDeclaredConstructor().newInstance();
    }
}
