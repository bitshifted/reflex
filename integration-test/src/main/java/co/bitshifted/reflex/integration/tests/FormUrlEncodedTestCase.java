/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.integration.tests;

import co.bitshifted.reflex.core.http.*;
import co.bitshifted.reflex.integration.Constants;
import co.bitshifted.reflex.integration.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static co.bitshifted.reflex.core.Reflex.client;
import static co.bitshifted.reflex.core.Reflex.context;

public class FormUrlEncodedTestCase implements TestCasePackage{

    private static final Logger LOGGER = LoggerFactory.getLogger(FormUrlEncodedTestCase.class);

    private static final String FORM_URLENCODED_POST = "post_form_www_urlencoded";

    @Override
    public List<TestResult> runTests() {
        var results = new ArrayList<TestResult>();
        results.add(submitFormUrlEncoded());
        return results;
    }

    private TestResult submitFormUrlEncoded() {
        var testResult = Constants.TEST_RESULT_FAIL;
        context().configuration().baseUri(Constants.SERVER_BASE_URL);
        try {
            var request =
                    RFXHttpRequestBuilder.newBuilder().method(RFXHttpMethod.POST).header(RFXHttpHeaders.CONTENT_TYPE, RFXMimeTypes.FORM_URLENCODED.toMimeTypeString()).path("/v1/form-post").build();
            var response = client().sendHttpRequest(request);
            if(Verifier.verify("Invalid response status", response.status() == RFXHttpStatus.OK)) {
                testResult = Constants.TEST_RESULT_SUCCESS;
            }
        } catch (Exception ex) {
            System.err.println("Failed to execute request: " + ex.getMessage());
        }
        return new TestResult(FORM_URLENCODED_POST, testResult);
    }

}
