package iad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class Controller {
    @FXML
    private ChoiceBox<String> choiceSeparator;
    ObservableList<String> separatorList = FXCollections.observableArrayList(",",".",";");

    @FXML
    private void initialize() {
        choiceSeparator.setItems(separatorList);
        choiceSeparator.setValue(",");
    }

}
