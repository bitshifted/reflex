/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.integration.tests.json;

import co.bitshifted.reflex.integration.TestResult;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GsonJsonTestCase extends BaseJsonTestCase {

  private static final Logger LOGGER = LoggerFactory.getLogger(JacksonJsonTestCase.class);
  private static final String JSON_GET_WITH_RESPONSE_BODY = "gson_get_json_with_response_body";
  private static final String JSON_POST_WITH_RESPONSE_BODY = "gson_post_json_with_response_body";

  @Override
  public List<TestResult> runTests() {
    var results = new ArrayList<TestResult>();
    results.add(getRequestReturnsResponseWithJsonBody(JSON_GET_WITH_RESPONSE_BODY));
    results.add(postRequestWithBodyReturnsResponseWithJsonBody(JSON_POST_WITH_RESPONSE_BODY));
    return results;
  }
}
