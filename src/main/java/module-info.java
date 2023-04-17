module co.bitshifted.libs.httpjfx {
    requires javafx.base;
    requires javafx.graphics;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    exports co.bitshifted.libs.reflex;
    exports co.bitshifted.libs.reflex.http;
    exports co.bitshifted.libs.reflex.serialize;
}