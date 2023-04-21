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

import co.bitshifted.reflex.integration.tests.TestCasePackage;
import co.bitshifted.reflex.integration.tests.TestSuite;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflexTester {

    public static void main(String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("At least one test suite name is required");
            System.exit(Constants.TEST_FAIL);
        }
        var testResults = new ArrayList<TestResult>();
        var testCases = Stream.of(args).map(arg -> TestSuite.valueOf(arg)).collect(Collectors.toList());
        for(TestSuite ts : testCases) {
            testResults.addAll(ts.getTestCasePackage().runTests());
        }

       testResults.forEach(tr -> System.out.println(tr.displayResult()));

       int finalTestResult = testResults.stream()
               .filter(tr -> tr.result() == Constants.TEST_FAIL).findFirst()
               .orElse(new TestResult("all", Constants.TEST_SUCCESS))
               .result();
      System.exit(finalTestResult);
    }
}
