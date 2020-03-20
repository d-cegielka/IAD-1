module org.openjfx {
    requires javafx.controls;
    requires javafx.swing;
    requires javafx.fxml;
    requires commons.math3;
    requires java.logging;
    requires univocity.parsers;

    opens iad to javafx.fxml;
    exports iad;
}