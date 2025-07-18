package co.bitshifted.reflex.core;

import static co.bitshifted.reflex.core.Reflex.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ReflexTest {

  @Test
  void shouldReturnDefaultContext() {
    var output = context();
    assertNotNull(output, "Default context should not be null");
  }

  @Test
  void shouldCreateNewContextWhenNotExists() {
    context("first").configuration().baseUri("http://example.com").disableSslCertVerification(true);
    context("second").configuration().baseUri("http://localhost");
    // verify first context configuration
    assertEquals(
        "http://example.com",
        context("first").configuration().baseUri().toString(),
        "First context base URI should be set");
    assertTrue(
        context("first").configuration().disableSslCertVerification(),
        "First context should have SSL verification disabled");
    // verify second context configuration
    assertEquals(
        "http://localhost",
        context("second").configuration().baseUri().toString(),
        "Second context base URI should be set");
    assertFalse(
        context("second").configuration().disableSslCertVerification(),
        "Second context should have SSL verification enabled by default");
  }

  @Test
  void shouldThrowExceptionForNonExistentContext() {
    String nonExistentContext = "nonExistent";
    assertThrows(
        IllegalArgumentException.class,
        () -> client(nonExistentContext),
        "Should throw exception when trying to access client of a non-existent context");
  }

  @Test
  void shouldReturnClientForContext() {
    context("newctx").configuration().baseUri("http://localhost");
    var client = client("newctx");
    assertNotNull(client);
  }
}
