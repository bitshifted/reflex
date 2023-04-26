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

public record TestResult(String name, int result) {

  public String displayResult(int len) {
    var resultString = (result == Constants.TEST_RESULT_SUCCESS) ? "SUCCESS" : "FAILURE";
    String formatString = "%-" + len + "s\t%s";

    var nameSb = new StringBuilder(Constants.ANSI_YELLOW).append(name);
    var resultSb = new StringBuilder();
    if (result == Constants.TEST_RESULT_SUCCESS) {
      resultSb.append(Constants.ANSI_GREEN).append(resultString);
    } else {
      resultSb.append(Constants.ANSI_RED).append(resultString);
    }
    resultSb.append(Constants.ANSI_RESET);
    return String.format(formatString, nameSb.toString(), resultSb.toString());
  }
}
