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
import static co.bitshifted.reflex.integration.tests.Verifier.*;

import co.bitshifted.reflex.core.Reflex;
import co.bitshifted.reflex.core.http.RFXHttpMethod;
import co.bitshifted.reflex.core.http.RFXHttpRequestBuilder;
import co.bitshifted.reflex.integration.Constants;
import co.bitshifted.reflex.integration.TestResult;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlainTextTestCase implements TestCasePackage {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlainTextTestCase.class);

  private static final String PLAIN_TEXT_GET = "plain_text_get";
  private static final String PLAIN_TEXT_GET_ASYNC = "plain_text_get_async";

  @Override
  public List<TestResult> runTests() {
    var results = new ArrayList<TestResult>();
    results.add(simpleGetPlainText());
    results.add(runAsyncGetShouldReturnSuccess());
    return results;
  }

  private TestResult simpleGetPlainText() {
    var testResult = Constants.TEST_RESULT_FAIL;
    context().configuration().baseUri(Constants.SERVER_BASE_URL).disableSslCertVerification(true);
    try {
      var request =
          RFXHttpRequestBuilder.newBuilder().method(RFXHttpMethod.GET).path("/v1/text").build();
      var response = client().sendHttpRequest(request);
      var responseBody = response.bodyTo(String.class);
      if ("Example plain text body".equals(responseBody)) {
        testResult = Constants.TEST_RESULT_SUCCESS;
      }
    } catch (Exception ex) {
      LOGGER.error("Failed to execute request", ex);
    }
    return new TestResult(PLAIN_TEXT_GET, testResult);
  }

  private TestResult runAsyncGetShouldReturnSuccess() {
    var result = Constants.TEST_RESULT_FAIL;
    Reflex.context().configuration().baseUri(Constants.SERVER_BASE_URL);
    var request =
        RFXHttpRequestBuilder.newBuilder().method(RFXHttpMethod.GET).path("/v1/text").build();
    try {
      var sb = new StringBuilder();
      CompletableFuture.allOf(
              client()
                  .sendHttpRequestAsync(request)
                  .thenAccept(r -> sb.append(r.bodyTo(String.class)).append("\n")),
              client()
                  .sendHttpRequestAsync(request)
                  .thenAccept(r -> sb.append(r.bodyTo(String.class)).append("\n")))
          .join();
      var output = sb.toString();
      var parts = output.split("\n");
      if (verify("Invalid output size", parts.length == 2)) {
        result = Constants.TEST_RESULT_SUCCESS;
      }

    } catch (Exception ex) {
      LOGGER.error("Failed to execute request", ex);
    }
    return new TestResult(PLAIN_TEXT_GET_ASYNC, result);
  }
}
