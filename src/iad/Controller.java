package iad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {
    private Data data;

    @FXML
    private ChoiceBox<String> choiceSeparator;
    ObservableList<String> separatorList = FXCollections.observableArrayList(",",".",";");

    @FXML
    private void initialize() {
        choiceSeparator.setItems(separatorList);
        choiceSeparator.setValue(",");
    }

    @FXML
    public void openFile(){
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import data");
        File file = fileChooser.showOpenDialog(new Stage());
        data = new Data(file,choiceSeparator.getValue());
    }


}
