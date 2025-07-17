/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.integration.tests.json;

import static co.bitshifted.reflex.integration.tests.Verifier.verify;
import static co.bitshifted.reflex.integration.tests.Verifier.verifyAll;

import co.bitshifted.reflex.core.Reflex;
import co.bitshifted.reflex.core.http.*;
import co.bitshifted.reflex.integration.Constants;
import co.bitshifted.reflex.integration.TestResult;
import co.bitshifted.reflex.integration.model.Address;
import co.bitshifted.reflex.integration.model.Person;
import co.bitshifted.reflex.integration.tests.TestCasePackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseJsonTestCase implements TestCasePackage {

  private static final Logger LOGGER = LoggerFactory.getLogger(BaseJsonTestCase.class);

  private static final String MAIN_STREET = "main street 21";

  public BaseJsonTestCase() {
    Reflex.context()
        .configuration()
        .baseUri(Constants.SERVER_BASE_URL)
        .disableSslCertVerification(true)
        .commonHeader(RFXHttpHeaders.ACCEPT, RFXMimeTypes.APPLICATION_JSON.toMimeTypeString());
  }

  protected TestResult getRequestReturnsResponseWithJsonBody(String testName) {
    var testResult = Constants.TEST_RESULT_FAIL;
    try {
      var request =
          RFXHttpRequestBuilder.newBuilder()
              .method(RFXHttpMethod.GET)
              .urlTemplate(
                  RFXHttpRequestBuilder.UrlTemplateBuilder.urlTemplate("/v1/persons/{id}")
                      .pathParam("id", "1"))
              .build();
      var response = Reflex.client().sendHttpRequest(request);
      if (response.status() == RFXHttpStatus.OK) {
        var person = response.bodyTo(Person.class);
        boolean result =
            verifyAll(
                person != null,
                "John Smith".equals(person.getName()),
                person.getAge() == 20,
                person.getAddress() != null,
                MAIN_STREET.equals(person.getAddress().getStreetAddress()));
        if (result) {
          testResult = Constants.TEST_RESULT_SUCCESS;
        }
      }
    } catch (Exception ex) {
      LOGGER.error("Failed to execute request", ex);
    }
    return new TestResult(testName, testResult);
  }

  protected TestResult postRequestWithBodyReturnsResponseWithJsonBody(String testName) {
    var testResult = Constants.TEST_RESULT_FAIL;
    var personIn = new Person();
    personIn.setName("Jane Doe");
    personIn.setAge(25);
    var address = new Address();
    address.setStreetAddress(MAIN_STREET);
    address.setZipCode("SD123");
    address.setCity("London");
    personIn.setAddress(address);
    try {
      var request =
          RFXHttpRequestBuilder.newBuilder(personIn)
              .method(RFXHttpMethod.POST)
              .path("/v1/persons")
              .header(RFXHttpHeaders.CONTENT_TYPE, RFXMimeTypes.APPLICATION_JSON.toMimeTypeString())
              .build();
      var response = Reflex.client().sendHttpRequest(request);
      if (response.status() == RFXHttpStatus.OK) {
        var personResponse = response.bodyTo(Person.class);
        boolean result =
            verifyAll(
                verify("Person response is null", personResponse != null),
                verify("Invalid person ID", personResponse.getId() == 100),
                verify("Invalid person name", "Jane Doe".equals(personResponse.getName())),
                verify("Invalid person age", personResponse.getAge() == 25),
                verify("Address is null", personResponse.getAddress() != null),
                verify(
                    "Invalid street address",
                    MAIN_STREET.equals(personResponse.getAddress().getStreetAddress())),
                verify(
                    "Invalid city name", "London".equals(personResponse.getAddress().getCity())));
        if (result) {
          testResult = Constants.TEST_RESULT_SUCCESS;
        }
      }
    } catch (Exception ex) {
      LOGGER.error("Failed to execute request", ex);
    }
    return new TestResult(testName, testResult);
  }
}
