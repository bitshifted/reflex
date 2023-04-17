/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.libs.reflex.http;

import java.util.Objects;

public record MimeType(String type, String subtype, String tree, String suffix, String param) {

    public MimeType {
        Objects.requireNonNull(type);
        Objects.requireNonNull(subtype);
    }

    public MimeType(String type, String subtype) {
        this(type, subtype, null, null, null);
    }

    public String value() {
        var sb = new StringBuilder(type);
        sb.append("/");
        if (tree != null && !tree.isBlank() && !tree.isEmpty()) {
            sb.append(tree).append(".");
        }
        sb.append(subtype);
        if (suffix != null && !suffix.isEmpty() && !suffix.isBlank()) {
            sb.append("+").append(suffix);
        }
        if (param != null && !param.isEmpty() && !param.isBlank()) {
            sb.append(";").append(param);
        }
        return sb.toString();
    }

}
