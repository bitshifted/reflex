/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.impl.urlconn;

import static co.bitshifted.reflex.core.Reflex.context;
import static co.bitshifted.reflex.core.http.RFXHttpMethod.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

import co.bitshifted.reflex.core.exception.HttpStatusException;
import co.bitshifted.reflex.core.http.*;
import co.bitshifted.reflex.core.serialize.PlainTextBodySerializer;
import co.bitshifted.reflex.core.serialize.file.FileDownloadDetails;
import co.bitshifted.reflex.core.serialize.file.FileOperationSerializer;
import co.bitshifted.reflex.core.serialize.file.FileUploadDetails;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;

@WireMockTest(httpPort = 9020)
public class HttpUrlConnectionClientTest {

  @Test
  void basicGetRequestPlaintextSuccess() throws Exception {
    stubFor(
        get("/test/endpoint")
            .withHeader("custom-header", equalTo("custom-value"))
            .willReturn(ok("test body").withHeader(RFXHttpHeaders.CONTENT_TYPE, "text/plain")));
    context().registerBodySerializer(RFXMimeTypes.TEXT_PLAIN, new PlainTextBodySerializer());
    context().configuration().commonHeader("custom-header", "custom-value");
    var client = new HttpUrlConnectionClient();
    var headers = new RFXHttpHeaders();
    headers.setHeader(RFXHttpHeaders.ACCEPT, RFXMimeTypes.TEXT_PLAIN.toMimeTypeString());
    headers.setHeader(RFXHttpHeaders.ACCEPT_LANGUAGE, "rn-US");
    var request =
        RFXHttpRequestBuilder.newBuilder()
            .method(GET)
            .requestUri(new URI("http://localhost:9020/test/endpoint"))
            .build();
    var response = client.sendHttpRequest(request);
    assertNotNull(response);
    assertNotNull(response.body());
    var responseBody = response.bodyTo(String.class);
    assertEquals("test body", responseBody);
  }

  @Test
  void basicPostRequestSuccess() throws Exception {
    stubFor(post("/test/post").willReturn(noContent()));
    context().registerBodySerializer(RFXMimeTypes.TEXT_PLAIN, new PlainTextBodySerializer());
    var client = new HttpUrlConnectionClient();
    //        var headers = new RFXHttpHeaders();
    //        headers.setHeader(RFXHttpHeaders.CONTENT_TYPE, RFXMimeTypes.TEXT_PLAIN.value());
    var request =
        RFXHttpRequestBuilder.newBuilder("body")
            .method(POST)
            .requestUri(new URI("http://localhost:9020/test/post"))
            .header(RFXHttpHeaders.CONTENT_TYPE, RFXMimeTypes.TEXT_PLAIN.toMimeTypeString())
            .build();
    var response = client.sendHttpRequest(request);
    assertNotNull(response);
    assertEquals(RFXHttpStatus.NO_CONTENT, response.status());
  }

  @Test
  void getRequestFailsWhenErrorStatusFromServer() throws Exception {
    stubFor(get("/test/failure").willReturn(notFound()));
    var client = new HttpUrlConnectionClient();
    var request =
        RFXHttpRequestBuilder.newBuilder()
            .method(GET)
            .requestUri(new URI("http://localhost:9020/test/failure"))
            .build();
    assertThrows(HttpStatusException.class, () -> client.sendHttpRequest(request));
  }

  @Test
  void getAsyncReturnSuccess() throws Exception {
    stubFor(
        get("/test/endpoint")
            .willReturn(ok("test body").withHeader(RFXHttpHeaders.CONTENT_TYPE, "text/plain")));
    context().registerBodySerializer(RFXMimeTypes.TEXT_PLAIN, new PlainTextBodySerializer());
    var client = new HttpUrlConnectionClient();
    var headers = new RFXHttpHeaders();
    headers.setHeader(RFXHttpHeaders.ACCEPT, RFXMimeTypes.TEXT_PLAIN.toMimeTypeString());
    headers.setHeader(RFXHttpHeaders.ACCEPT_LANGUAGE, "rn-US");
    var request =
        RFXHttpRequestBuilder.newBuilder()
            .method(GET)
            .requestUri(new URI("http://localhost:9020/test/endpoint"))
            .build();
    var response = client.sendHttpRequestAsync(request).get();
    assertNotNull(response);
    assertNotNull(response.body());
    var responseBody = response.bodyTo(String.class);
    assertEquals("test body", responseBody);
  }

  @Test
  void fileUploadWithProgressSuccess() throws Exception {
    stubFor(
        post("/file-upload")
            .withHeader("Content-Type", equalTo("application/octet-stream"))
            .willReturn(noContent()));
    var uploadFile = Files.createTempFile("test", "upload");
    System.out.println("upload file: " + uploadFile.toAbsolutePath());
    System.out.println("Creating random content");
    var random = new Random();
    var data = new byte[8192];
    for (int i = 0; i < 1000; i++) {
      random.nextBytes(data);
      Files.write(uploadFile, data, StandardOpenOption.APPEND);
    }
    var fileDetails = new FileUploadDetails(uploadFile);
    context().configuration().baseUri("http://localhost:9020");
    context()
        .registerBodySerializer(
            RFXMimeTypes.fromString("application/octet-stream"), new FileOperationSerializer());
    var request =
        RFXHttpRequestBuilder.newBuilder(fileDetails)
            .method(POST)
            .header(RFXHttpHeaders.CONTENT_TYPE, "application/octet-stream")
            .urlTemplate(RFXHttpRequestBuilder.UrlTemplateBuilder.urlTemplate("/file-upload"))
            .build();
    var client = new HttpUrlConnectionClient();
    var response = client.sendHttpRequestAsync(request);
    CompletableFuture.runAsync(
        () -> {
          System.out.println("total bytes: " + fileDetails.getFileSize());
          while (fileDetails.getReadBytesCount() < fileDetails.getFileSize()) {
            System.out.println("read bytes count: " + fileDetails.getReadBytesCount());
          }
        });
    var result = response.get();
    assertEquals(RFXHttpStatus.NO_CONTENT, result.status());
    assertEquals(fileDetails.getFileSize(), fileDetails.getReadBytesCount());
  }

  @Test
  void fileDownloadWithProgressSuccess() throws Exception {
    var sourceFile = Files.createTempFile("test", "upload");
    System.out.println("Creating random content");
    var random = new Random();
    var data = new byte[8192];
    for (int i = 0; i < 1000; i++) {
      random.nextBytes(data);
      Files.write(sourceFile, data, StandardOpenOption.APPEND);
    }
    // setup wiremock
    stubFor(
        get("/file-download")
            .willReturn(
                aResponse()
                    .withHeader("Content-type", "application/octet-stream")
                    .withHeader("Content-Length", Long.toString(sourceFile.toFile().length()))
                    .withBody(Files.readAllBytes(sourceFile))));
    context().configuration().baseUri("http://localhost:9020");
    context()
        .registerBodySerializer(
            RFXMimeTypes.fromString("application/octet-stream"), new FileOperationSerializer());
    var request =
        RFXHttpRequestBuilder.newBuilder()
            .method(GET)
            .urlTemplate(RFXHttpRequestBuilder.UrlTemplateBuilder.urlTemplate("/file-download"))
            .build();
    var client = new HttpUrlConnectionClient();
    var response = client.sendHttpRequest(request);
    var fileDetails = response.bodyTo(FileDownloadDetails.class);
    var downloadFile = Files.createTempFile("test", "download");
    fileDetails.setFilePath(downloadFile);
    var downloadResult = fileDetails.download();
    CompletableFuture.runAsync(
        () -> {
          while (fileDetails.getWrittenBytesCount() < fileDetails.getContentLength()) {
            System.out.println("written bytes count: " + fileDetails.getWrittenBytesCount());
          }
        });
    long bytes = downloadResult.get();
    assertEquals(sourceFile.toFile().length(), downloadFile.toFile().length());
    assertEquals(sourceFile.toFile().length(), bytes);
  }
}
