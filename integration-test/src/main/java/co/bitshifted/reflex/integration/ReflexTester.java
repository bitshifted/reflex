/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.integration;

import co.bitshifted.reflex.integration.tests.TestSuite;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflexTester {

  private static final int DEFAULT_MAX_LEN = 80;

  public static void main(String... args) throws Exception {
    if (args.length == 0) {
      System.out.println("At least one test suite name is required");
      System.exit(Constants.TEST_RESULT_FAIL);
    }
    var testResults = new ArrayList<TestResult>();
    var testCases = Stream.of(args).map(arg -> TestSuite.valueOf(arg)).collect(Collectors.toList());
    for (TestSuite ts : testCases) {
      testResults.addAll(ts.getTestCasePackage().runTests());
    }
    var maxLenResult = testResults.stream().max(Comparator.comparingInt(tr -> tr.name().length()));
    int maxLen;
    if (maxLenResult.isPresent()) {
      maxLen = maxLenResult.get().name().length();
    } else {
      maxLen = DEFAULT_MAX_LEN;
    }

    testResults.forEach(
        tr -> System.out.println(tr.displayResult(maxLen + Constants.OUTPUT_PADDING)));

    int finalTestResult =
        testResults.stream()
            .filter(tr -> tr.result() == Constants.TEST_RESULT_FAIL)
            .findFirst()
            .orElse(new TestResult("all", Constants.TEST_RESULT_SUCCESS))
            .result();
    System.exit(finalTestResult);
  }
}
