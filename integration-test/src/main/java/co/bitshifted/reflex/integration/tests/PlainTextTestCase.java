/*
 *
 *  * Copyright (c) 2023-2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.integration.tests;

import static co.bitshifted.reflex.core.Reflex.*;

import co.bitshifted.reflex.core.http.RFXHttpMethod;
import co.bitshifted.reflex.core.http.RFXHttpRequestBuilder;
import co.bitshifted.reflex.integration.Constants;
import co.bitshifted.reflex.integration.TestResult;
import java.util.ArrayList;
import java.util.List;

public class PlainTextTestCase implements TestCasePackage {

  private static final String JDK_11_PLAIN_TEXT_GET = "jdk11_plain_text_get";

  @Override
  public List<TestResult> runTests() {
    var results = new ArrayList<TestResult>();
    var testResult = Constants.TEST_RESULT_FAIL;
    context().configuration().baseUri(Constants.SERVER_BASE_URL);
    try {
      var request =
          RFXHttpRequestBuilder.newBuilder().method(RFXHttpMethod.GET).path("/v1/text").build();
      var response = client().sendHttpRequest(request);
      var responseBody = response.bodyToValue(String.class);
      if ("Example plain text body".equals(responseBody)) {
        testResult = Constants.TEST_RESULT_SUCCESS;
      }
    } catch (Exception ex) {
      System.err.println("Failed to execute request: " + ex.getMessage());
    }
    results.add(new TestResult(JDK_11_PLAIN_TEXT_GET, testResult));
    return results;
  }
}
