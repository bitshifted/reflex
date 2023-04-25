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

import co.bitshifted.reflex.core.Reflex;
import co.bitshifted.reflex.core.http.*;
import co.bitshifted.reflex.integration.Constants;
import co.bitshifted.reflex.integration.TestResult;
import co.bitshifted.reflex.integration.model.Address;
import co.bitshifted.reflex.integration.model.Person;

import java.util.ArrayList;
import java.util.List;

import static co.bitshifted.reflex.integration.tests.Verifier.*;

public class jacksonJsonTestCase implements TestCasePackage {

    private static final String JSON_GET_WITH_RESPONSE_BODY = "jdk11_jackson_get_json_with_response_body";
    private static final String JSON_POST_WITH_RESPONSE_BODY = "jdk11_post_json_with_response_body";


    public jacksonJsonTestCase() {
        Reflex.context().configuration().baseUri(Constants.SERVER_BASE_URL);
    }

    @Override
    public List<TestResult> runTests() {
        var results = new ArrayList<TestResult>();
        results.add(getRequestReturnsResponseWithJsonBody());
        results.add(postRequestWithBodyReturnsResponseWithJsonBody());
        return results;
    }

    private TestResult getRequestReturnsResponseWithJsonBody() {
        var testResult = Constants.TEST_RESULT_FAIL;
        try {
            var request = RFXHttpRequestBuilder.newBuilder()
                    .method(RFXHttpMethod.GET)
                    .path("/v1/persons/1")
                    .build();
            var response = Reflex.client().sendHttpRequest(request);
            if(response.status() == RFXHttpStatus.OK) {
                var person = response.bodyToValue(Person.class);
                boolean result = verifyAll(person != null, "John Smith".equals(person.getName()), person.getAge() == 20
                    ,person.getAddress() != null,  "main street 21".equals(person.getAddress().getStreetAddress()));
                if(result) {
                    testResult = Constants.TEST_RESULT_SUCCESS;
                }

            }
        } catch(Exception ex) {
            System.err.println("Failed to execute request: " + ex.getMessage());
            ex.printStackTrace();
        }
        return new TestResult(JSON_GET_WITH_RESPONSE_BODY, testResult);
    }

    private TestResult postRequestWithBodyReturnsResponseWithJsonBody() {
        var testResult = Constants.TEST_RESULT_FAIL;
        var personIn = new Person();
        personIn.setName("Jane Doe");
        personIn.setAge(25);
        var address = new Address();
        address.setStreetAddress("main street 21");
        address.setZipCode("SD123");
        address.setCity("London");
        personIn.setAddress(address);
        try {
            var request = RFXHttpRequestBuilder.newBuilder(personIn)
                    .method(RFXHttpMethod.POST)
                    .path("/v1/persons")
                    .header(RFXHttpHeaders.CONTENT_TYPE, RFXMimeTypes.APPLICATION_JSON.toMimeTypeString())
                    .build();
            var response = Reflex.client().sendHttpRequest(request);
            if(response.status() == RFXHttpStatus.OK) {
                var personResponse = response.bodyToValue(Person.class);
                boolean result = verifyAll(
                        verify("Person response is null", personResponse != null),
                        verify("Invalid person ID", personResponse.getId() == 100) ,
                        verify("Invalid person name", "Jane Doe".equals(personResponse.getName())),
                        verify("Invalid person age",personResponse.getAge() == 25),
                        verify("Address is null",personResponse.getAddress() != null),
                        verify("Invalid street address", "main street 21".equals(personResponse.getAddress().getStreetAddress())),
                        verify("Invalid city name", "London".equals(personResponse.getAddress().getCity())));
                if(result) {
                    testResult = Constants.TEST_RESULT_SUCCESS;
                }

            }
        } catch(Exception ex) {
            System.err.println("Failed to execute request: " + ex.getMessage());
            ex.printStackTrace();
        }
        return new TestResult(JSON_POST_WITH_RESPONSE_BODY, testResult);
    }
}
