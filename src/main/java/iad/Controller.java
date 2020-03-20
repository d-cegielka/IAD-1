package iad;

import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    private Data analyzedData;
    private Calculations calcs;
    private StringBuilder report;
    private File inputFile;

    @FXML
    private ChoiceBox<Integer> choiceColumn;

    @FXML
    private TextField alphaValue;

    @FXML
    private TextArea reportArea;

    @FXML
    private ChoiceBox<String> choiceC1;

    @FXML
    private ChoiceBox<String> choiceC2;

    @FXML
    private void initialize() {
    }

    @FXML
    public void openFile(){
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj dane");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki tekstowe (*.txt, *.csv)", "*.txt", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        /*String path = "C:\\IAD\\224546.txt";
        inputFile = new File(path);*/
        inputFile = fileChooser.showOpenDialog(new Stage());

        //detekcja separatora
        CsvParserSettings settings = new CsvParserSettings();
        settings.detectFormatAutomatically();
        CsvParser parser = new CsvParser(settings);
        //List<String[]> rows = parser.parseAll(inputFile);
        parser.parse(inputFile);
        CsvFormat separatorDetected = parser.getDetectedFormat();

        analyzedData = new Data(inputFile,separatorDetected.getDelimiterString());
        calcs = new Calculations(analyzedData.getData(), analyzedData.getClassTypes());

        //Wybór kolumny do analizy
        for (int i = 0; i < analyzedData.getNumberOfAttribute() - 1; i++)
            choiceColumn.getItems().add(i);

        choiceColumn.setDisable(false);
        choiceColumn.getSelectionModel().select(0);

        //Wybór klas do testowania hipotez
        choiceC1.setItems(FXCollections.observableArrayList(analyzedData.getClassTypes()));
        choiceC1.setDisable(false);
        choiceC1.getSelectionModel().select(0);
        choiceC2.setItems(FXCollections.observableArrayList(analyzedData.getClassTypes()));
        choiceC2.setDisable(false);
        choiceC2.getSelectionModel().select(1);
    }

    @FXML
    public void analyseData(){
        report = new StringBuilder();
        if(alphaValue.getText().isEmpty()) alphaValue.setText(alphaValue.getPromptText());
        report.append("Plik źródłowy: ").append(inputFile.getName()).append(System.lineSeparator());
        report.append("Separator: [").append(analyzedData.getSeparator()).append("]").append(System.lineSeparator());
        report.append("Analizowane klasy: ").append(analyzedData.getClassTypes()).append(System.lineSeparator());
        report.append(calcs.analyseData(Double.parseDouble(alphaValue.getText()), choiceColumn.getValue(), choiceC1.getValue(), choiceC2.getValue()));
        reportArea.setText(report.toString());
    }

    @FXML
    public void saveDataToFile(){
        if(reportArea.getText().isEmpty()) analyseData();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz dane");

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki tekstowe (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            saveTextToFile(report.toString(), file);
        }
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
