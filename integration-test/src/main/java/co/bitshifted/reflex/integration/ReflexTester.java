/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.integration;

import co.bitshifted.reflex.Reflex;
import co.bitshifted.reflex.http.RFXHttpMethod;
import co.bitshifted.reflex.http.RFXHttpRequestBuilder;

import java.net.URI;
import java.util.ArrayList;

public class ReflexTester {

    public static void main(String... args) throws Exception {
       var testResults = new ArrayList<TestResult>();
       var jdk11PlainTextTester = new Jdk11ClientPlainTextTester();
       testResults.addAll(jdk11PlainTextTester.runTests());

       testResults.forEach(tr -> System.out.println(tr.displayResult()));

       int finalTestResult = testResults.stream()
               .filter(tr -> tr.result() == Constants.TEST_FAIL).findFirst()
               .orElse(new TestResult("all", Constants.TEST_SUCCESS))
               .result();
      System.exit(finalTestResult);
    }
}
