/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.serialize.file;

import co.bitshifted.reflex.core.exception.BodySerializationException;
import co.bitshifted.reflex.core.http.RFXMimeType;
import co.bitshifted.reflex.core.http.RFXMimeTypes;
import co.bitshifted.reflex.core.serialize.BodySerializer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class FileOperationSerializer implements BodySerializer {
    @Override
    public Set<RFXMimeType> supportedMimeTypes() {
        return Set.of(RFXMimeTypes.fromString("application/*"), RFXMimeTypes.fromString("image/*"), RFXMimeTypes.fromString("video/*"));
    }

    @Override
    public <T> InputStream objectToStream(T object) {
        if (object instanceof FileUploadDetails fd) {
            try {
                return fd.getMonitoringInputStream();
            } catch(FileNotFoundException ex) {
                throw new BodySerializationException(ex);
            }

        }
        throw new BodySerializationException("Invalid object type. Expecting FileUploadDetails");
    }

    @Override
    public <T> T streamToObject(InputStream input, Class<T> type) {
        return this.streamToObject(input, type, -1);
    }

    @Override
    public <T> T streamToObject(InputStream input, Class<T> type, long contentLength) {
        if(type == FileDownloadDetails.class) {
            try {
                var constructor = type.getConstructor(InputStream.class, Long.class);
                return constructor.newInstance(input, contentLength);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException |
                     InvocationTargetException ex) {
                throw new BodySerializationException(ex);
            }
        }
        throw new BodySerializationException("Invalid object type. Expecting FileDownloadDetails");
    }
}
