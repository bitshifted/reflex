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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class ProgressMonitorInputStream extends FileInputStream {

    private volatile long readCount = 0;

    public ProgressMonitorInputStream(Path path) throws FileNotFoundException {
        super(path.toFile());
    }

    @Override
    public int read() throws IOException {
        int count = super.read();
        if(count > -1) {
            readCount += count;
        }
        return count;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int count =  super.read(b);
        if(count > -1) {
            readCount += count;
        }
        return count;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count =  super.read(b, off, len);
        if(count > -1) {
            readCount += count;
        }
        return count;
    }


    public long getReadCount() {
        return readCount;
    }
}
