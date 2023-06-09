module co.bitshifted.libs.httpjfx {
  requires static java.net.http;
  requires static com.fasterxml.jackson.databind;
  requires static com.fasterxml.jackson.dataformat.xml;
  requires static com.google.gson;
  requires static jakarta.xml.bind;
  requires org.slf4j;

  exports co.bitshifted.reflex.core.http;
  exports co.bitshifted.reflex.core.config;
  exports co.bitshifted.reflex.core.serialize;
  exports co.bitshifted.reflex.core;
  exports co.bitshifted.reflex.core.serialize.json;
  exports co.bitshifted.reflex.core.serialize.xml;
  exports co.bitshifted.reflex.core.serialize.form;
}
