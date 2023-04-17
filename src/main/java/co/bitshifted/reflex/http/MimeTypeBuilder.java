/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.http;

public class MimeTypeBuilder {

    private String type;
    private String subtype;
    private String tree;
    private String suffix;
    private String parameter;

    public MimeTypeBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public MimeTypeBuilder withSubtype(String subtype) {
        this.subtype = subtype;
        return this;
    }

    public MimeTypeBuilder withTree(String tree) {
        this.tree = tree;
        return this;
    }

    public MimeTypeBuilder withSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public MimeTypeBuilder withParameter(String parameter) {
        this.parameter = parameter;
        return this;
    }

    public MimeType build() {
        return new MimeType(type, subtype, tree, suffix, parameter);
    }
    public static MimeTypeBuilder newBuilder() {
        return new MimeTypeBuilder();
    }
}
