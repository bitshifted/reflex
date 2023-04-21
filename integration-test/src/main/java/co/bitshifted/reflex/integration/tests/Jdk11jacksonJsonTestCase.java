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
import co.bitshifted.reflex.core.http.RFXHttpMethod;
import co.bitshifted.reflex.core.http.RFXHttpRequestBuilder;
import co.bitshifted.reflex.core.http.RFXHttpStatus;
import co.bitshifted.reflex.integration.Constants;
import co.bitshifted.reflex.integration.TestResult;
import co.bitshifted.reflex.integration.model.Person;
import co.bitshifted.reflex.integration.tests.TestCasePackage;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Jdk11jacksonJsonTestCase implements TestCasePackage {

    private static final String JSON_GET_WITH_RESPONSE_BODY = "jdk11_jackson_get_json_with_response_body";


    @Override
    public List<TestResult> runTests() {
        var results = new ArrayList<TestResult>();
        results.add(getRequestReturnsResponseWithJsonBody());
        return results;
    }

    private TestResult getRequestReturnsResponseWithJsonBody() {
        var testResult = Constants.TEST_FAIL;
        try {
            var request = RFXHttpRequestBuilder.newBuilder()
                    .method(RFXHttpMethod.GET)
                    .requestUri(new URI("http://localhost:9000/v1/person/1")).build();
            var response = Reflex.client().sendHttpRequest(request);
            if(response.status() == RFXHttpStatus.OK) {
                var person = response.bodyToValue(Person.class);
                boolean result = Verifier.verifyAll(person != null, "John Smith".equals(person.getName()), person.getAge() == 20
                    ,person.getAddress() != null,  "main street 21".equals(person.getAddress().getStreetAddress()));
                if(result) {
                    testResult = Constants.TEST_SUCCESS;
                }

            }
        } catch(Exception ex) {
            System.err.println("Failed to execute request: " + ex.getMessage());
            ex.printStackTrace();
        }
        return new TestResult(JSON_GET_WITH_RESPONSE_BODY, testResult);
    }
}
