/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.serialize.xml;

import co.bitshifted.reflex.core.exception.BodySerializationException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Supplier;

public class JacksonXmlBodySerializer implements XmlBodySerializer {

    private final XmlMapper xmlMapper;

    public JacksonXmlBodySerializer() {
        this.xmlMapper = new XmlMapper();
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public JacksonXmlBodySerializer(Supplier<XmlMapper> supplier) {
        this.xmlMapper = supplier.get();
    }

    @Override
    public <T> InputStream objectToStream(T object) {
        try {
            byte[] dataBytes = xmlMapper.writeValueAsBytes(object);
            return new ByteArrayInputStream(dataBytes);
        } catch (Exception ex) {
            throw new BodySerializationException(ex);
        }
    }

    @Override
    public <T> T streamToObject(InputStream input, Class<T> type) {
        try {
            return xmlMapper.readValue(input, type);
        } catch (Exception ex) {
            throw new BodySerializationException(ex);
        }
    }
}
