module co.bitshifted.libs.httpjfx {
    requires static java.net.http;
    requires static com.fasterxml.jackson.databind;
    requires org.slf4j;

    exports co.bitshifted.reflex.core.http;
    exports co.bitshifted.reflex.core.serialize;
    exports co.bitshifted.reflex.core;
}