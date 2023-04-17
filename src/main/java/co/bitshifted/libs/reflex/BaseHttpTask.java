/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.libs.reflex;

import javafx.concurrent.Task;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public abstract class BaseHttpTask<T> extends Task<T> {

    protected final HttpClient httpCLient;
    protected final HttpRequest request;

    public BaseHttpTask(HttpRequest request) {
        httpCLient = HttpClient.newBuilder().build();
        this.request = request;
    }

}
