module org.openjfx {
    requires javafx.controls;
    requires com.google.common;
    requires javafx.swing;
    requires javafx.fxml;
    requires commons.math3;
    requires univocity.parsers;

    opens iad to javafx.fxml;
    exports iad;
}