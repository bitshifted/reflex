package co.bitshifted.reflex.integration.tests;

import static co.bitshifted.reflex.core.Reflex.client;
import static co.bitshifted.reflex.core.Reflex.context;

import co.bitshifted.reflex.core.http.RFXHttpMethod;
import co.bitshifted.reflex.core.http.RFXHttpRequestBuilder;
import co.bitshifted.reflex.core.http.RFXHttpStatus;
import co.bitshifted.reflex.integration.Constants;
import co.bitshifted.reflex.integration.TestResult;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiContextTestCase implements TestCasePackage {

  private static final Logger LOGGER = LoggerFactory.getLogger(MultiContextTestCase.class);

  public MultiContextTestCase() {
    context("one")
        .configuration()
        .baseUri(Constants.SERVER_BASE_URL)
        .disableSslCertVerification(true);
    context("two").configuration().baseUri(Constants.SERVER_BASE_URL);
  }

  @Override
  public List<TestResult> runTests() {
    return List.of(ctxOneShouldSucceed(), ctxTwoShouldFail());
  }

  private TestResult ctxOneShouldSucceed() {
    var testResult = Constants.TEST_RESULT_FAIL;
    try {
      var request =
          RFXHttpRequestBuilder.newBuilder().method(RFXHttpMethod.GET).path("/v1/text").build();
      var response = client("one").sendHttpRequest(request);
      if (response.status() == RFXHttpStatus.OK) {
        testResult = Constants.TEST_RESULT_SUCCESS;
      }
    } catch (Exception ex) {
      LOGGER.error("Failed to execute request in context one", ex);
    }
    return new TestResult("test_in_ctx_one", testResult);
  }

  private TestResult ctxTwoShouldFail() {
    var testResult = Constants.TEST_RESULT_FAIL;
    try {
      var request =
          RFXHttpRequestBuilder.newBuilder().method(RFXHttpMethod.GET).path("/v1/text").build();
      var response = client("two").sendHttpRequest(request);
    } catch (Exception ex) {
      LOGGER.error("Expected failure in context two", ex);
      testResult = Constants.TEST_RESULT_SUCCESS;
    }
    return new TestResult("test_in_ctxt_two", testResult);
  }
}
