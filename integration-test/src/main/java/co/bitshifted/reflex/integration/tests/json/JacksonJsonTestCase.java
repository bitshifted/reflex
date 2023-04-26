/*
 *
 *  * Copyright (c) 2023-2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.integration.tests.json;

import co.bitshifted.reflex.integration.TestResult;
import java.util.ArrayList;
import java.util.List;

public class JacksonJsonTestCase extends BaseJsonTestCase {

  private static final String JSON_GET_WITH_RESPONSE_BODY = "jackson_get_json_with_response_body";
  private static final String JSON_POST_WITH_RESPONSE_BODY = "jackson_post_json_with_response_body";

  @Override
  public List<TestResult> runTests() {
    var results = new ArrayList<TestResult>();
    results.add(getRequestReturnsResponseWithJsonBody(JSON_GET_WITH_RESPONSE_BODY));
    results.add(postRequestWithBodyReturnsResponseWithJsonBody(JSON_POST_WITH_RESPONSE_BODY));
    return results;
  }
}
