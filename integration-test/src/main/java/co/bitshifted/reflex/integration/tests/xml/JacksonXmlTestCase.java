/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.integration.tests.xml;

import co.bitshifted.reflex.integration.TestResult;
import java.util.ArrayList;
import java.util.List;

public class JacksonXmlTestCase extends BaseXmlTestCase {
  private static final String XML_GET_WITH_RESPONSE_BODY = "jackson_xml_get_xml_with_response_body";
  private static final String XML_POST_WITH_RESPONSE_BODY =
      "jackson_xml_post_xml_with_response_body";

  @Override
  public List<TestResult> runTests() {
    var results = new ArrayList<TestResult>();
    results.add(getRequestReturnsResponseWithXmlBody(XML_GET_WITH_RESPONSE_BODY));
    results.add(postRequestWithXMlBodyReturnsResponseWithXMlBody(XML_POST_WITH_RESPONSE_BODY));
    return results;
  }
}
