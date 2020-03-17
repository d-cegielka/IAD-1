package iad;

import iad.Calculations;
import iad.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {
    private Data analyzedData;
    private Calculations calcs;

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
        /*File file = fileChooser.showOpenDialog(new Stage());*/
        String path = "C:\\IAD\\data.txt";
        File file = new File(path);
        analyzedData = new Data(file,choiceSeparator.getValue());
        calcs = new Calculations(analyzedData.getData(), analyzedData.getClassTypes());
    }

    @FXML
    public void analyseData(){

    }


}
