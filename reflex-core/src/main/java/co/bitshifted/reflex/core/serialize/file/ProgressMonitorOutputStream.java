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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class ProgressMonitorOutputStream extends FileOutputStream {

    private long writeCount = 0;

    public ProgressMonitorOutputStream(Path path) throws FileNotFoundException {
        super(path.toFile());
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        writeCount++;
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        writeCount += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        writeCount += len;
    }

    public long getWriteCount() {
        return writeCount;
    }
}
