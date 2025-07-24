/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.http;

public final class RFXMimeTypes {

  private RFXMimeTypes() {}

  public static final String TYPE_APPLICATION = "application";

  public static final RFXMimeType APPLICATION_JSON = new RFXMimeType(TYPE_APPLICATION, "json");
  public static final RFXMimeType APPLICATION_XML = new RFXMimeType(TYPE_APPLICATION, "xml");
  public static final RFXMimeType TEXT_PLAIN = new RFXMimeType("text", "plain");
  public static final RFXMimeType FORM_URLENCODED =
      new RFXMimeType(TYPE_APPLICATION, "x-www-form-urlencoded");

  public static RFXMimeType fromString(String mimeType) {
    var paramParts = mimeType.split(";");
    String param = null;
    if (paramParts.length == 2) {
      param = paramParts[1].trim();
    }
    var suffixParts = paramParts[0].split("\\+");
    String suffix = null;
    if (suffixParts.length == 2) {
      suffix = suffixParts[1];
    }
    var typeParts = suffixParts[0].split("/");
    var type = typeParts[0];
    var subtypePart = typeParts[1];
    var vendorBoundary = subtypePart.indexOf(".");
    String tree = null;
    String subtype = subtypePart;
    if (vendorBoundary > 0) {
      tree = subtypePart.substring(0, vendorBoundary);
      subtype = subtypePart.substring(vendorBoundary + 1);
    }
    return new RFXMimeType(type, subtype, tree, suffix, param);
  }
}
