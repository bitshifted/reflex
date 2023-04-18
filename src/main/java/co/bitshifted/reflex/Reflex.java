/*
 *
 *  * Copyright (c) 2023-2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex;

public class Reflex {

    private static final Reflex INSTANCE;

    static {
        INSTANCE = new Reflex();
    }

    private final ReflexContext context;

    private Reflex() {
        this.context = new ReflexContext();
    }

    public static ReflexContext context() {
        return INSTANCE.context;
    }

    public static ReflexClient client() {
        return INSTANCE.context.defaultClient();
    }
}
