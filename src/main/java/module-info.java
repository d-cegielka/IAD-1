module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;
    requires java.logging;

    opens iad to javafx.fxml;
    exports iad;
}