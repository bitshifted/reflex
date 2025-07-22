# Reflex

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=bitshifted_reflex&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=bitshifted_reflex)
[![Release new version to Maven Central](https://github.com/bitshifted/reflex/actions/workflows/build-pipeline.yml/badge.svg)](https://github.com/bitshifted/reflex/actions/workflows/build-pipeline.yml)
[![License: MPL 2.0](https://img.shields.io/badge/License-MPL_2.0-brightgreen.svg)](https://opensource.org/licenses/MPL-2.0)

Simple, flexible and easy to use REST client library for Java. Main goal of this library is to provide 
lightweight client with minimal dependencies, which can be used in any Java application.

## Features

- Fluent API for making HTTP requests
- Support for common HTTP methods (GET, POST, PUT, DELETE, etc.)
- JSON serialization/deserialization, with support for Jackson and Gson
- XML serialization/deserialization with Jackson XML and JAXB
- Support for multipart/form-data requests with automatic object field conversion
- Custom header management
- SSL/TLS support with certificate validation options
- Configurable timeouts and redirects
- Built on Java's native HttpClient

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>co.bitshifted</groupId>
    <artifactId>reflex-core</artifactId>
    <version>${reflex.version}</version>
</dependency>
```

## Usage

To make a simple GET request, you can use the following code snippet:

```java

// make a GET request to get plain text
var request = RFXHttpRequestBuilder.newBuilder().
    method(RFXHttpMethod.GET).
    requesiUri(URI.create("http://example.com")).
    build();
var response = Reflex.client().sendHttpRequest(request);
var responseBody = response.bodyTo(String.class);

// use async client call
Reflex.client().sendHttpRequestAsync(request).thenAccept(r -> System.out.println(r.bodyTo(String.class)));
```

## JSON and XML support

Reflex client provides built-in support for JSON and XML serialization/deserialization. It uses what ever libraries are available on the classpath.
Out of the box it supports Jackson and Gson for JSON, and Jackson XML and JAXB for XML.

When successfull response is received, Reflex will inspect `Content-Type` header and use appropriate deserializer to convert response body to the requested type. See
the following code snippet for example:

```java
record User(String name, int age) {}

var request = RFXHttpRequestBuilder.newBuilder().
    method(RFXHttpMethod.GET).
    requesiUri(URI.create("http://example.com/user")).
    build();
var response = Reflex.client().sendHttpRequest(request);
var user = response.bodyTo(User.class); // automatically deserializes JSON or XML response to User object
```


# Configuration 

```java

## Configuration

Each instance of client is associated with a context. Context holds basic configuration for the client. 
Sample configuration can be done as follows:

```java
Reflex.context().configuration().
    baseUri("http://example.com").
    connectTimeout(Duration.ofSeconds(10)).
    disableSslVerification(true);
    
```

## License

This project is licensed under the Mozilla Public License 2.0 - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Links

- [Issue Tracker](https://github.com/bitshifted/reflex/issues)
- [Maven Central](https://search.maven.org/search?q=g:co.bitshifted%20AND%20a:reflex)