/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.serialize.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class FileDownloadDetails {

  private final InputStream source;
  private final long contentLength;
  private Path filePath;
  private ProgressMonitorOutputStream outputStream;

  public FileDownloadDetails(InputStream source, Long contentLength) {
    this.source = source;
    this.contentLength = contentLength;
  }

  public void setFilePath(Path filePath) throws IOException {
    this.filePath = filePath;
    if (Files.notExists(filePath)) {
      Files.createFile(filePath);
    }
    this.outputStream = new ProgressMonitorOutputStream(this.filePath);
  }

  public long getWrittenBytesCount() {
    return outputStream.getWriteCount();
  }

  public long getContentLength() {
    return contentLength;
  }

  public CompletableFuture<Long> download() {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            long result = source.transferTo(outputStream);
            source.close();
            outputStream.close();
            return result;
          } catch (IOException ex) {
            throw new CompletionException(ex);
          }
        });
  }
}
