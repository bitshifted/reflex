/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.exception;

public class BodySerializationException extends RuntimeException {

  public BodySerializationException(Throwable cause) {
    super(cause);
  }

  public BodySerializationException(String message) {
    super(message);
  }
}
