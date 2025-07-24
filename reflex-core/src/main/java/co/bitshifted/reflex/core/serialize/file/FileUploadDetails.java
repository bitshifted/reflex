/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.serialize.file;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public class FileUploadDetails {

  private final ProgressMonitorInputStream inputStream;
  private final long fileSize;
  private final Path path;

  public FileUploadDetails(Path path) throws FileNotFoundException {
    this.path = path;
    this.inputStream = new ProgressMonitorInputStream(path);
    this.fileSize = path.toFile().length();
  }

  public ProgressMonitorInputStream getMonitoringInputStream() {
    return inputStream;
  }

  public long getReadBytesCount() {
    return inputStream.getReadCount();
  }

  public long getFileSize() {
    return fileSize;
  }
}
