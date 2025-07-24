/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.integration.tests;

public class Verifier {

  public static boolean verifyAll(boolean... checks) {
    boolean result = true;
    for (boolean ch : checks) {
      result &= ch;
    }
    return result;
  }

  public static boolean verify(String message, boolean check) {
    if (!check) {
      System.err.println("Verification error: " + message);
    }
    return check;
  }
}
