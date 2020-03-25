package iad;

import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.google.common.io.Files;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Controller {
    private Charts analyzedData;
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
    private void initialize() { }

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

        try {
            analyzedData = new Charts(inputFile, separatorDetected.getDelimiterString());
        } catch (IOException e) {
            alertHandling(e.getMessage());
        }
        //calcs = new Calculations(analyzedData.getData(), analyzedData.getClassTypes());

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
        try {
            if (analyzedData == null) {
                throw new RuntimeException("Nie wczytano pliku z danymi!");
            }
            report = new StringBuilder();
            if (alphaValue.getText().isEmpty()) alphaValue.setText(alphaValue.getPromptText());
            report.append("Plik źródłowy: ").append(inputFile.getName()).append(System.lineSeparator());
            report.append("Separator: [").append(analyzedData.getSeparator()).append("]").append(System.lineSeparator());
            report.append("Analizowane klasy: ").append(analyzedData.getClassTypes()).append(System.lineSeparator());
            report.append(analyzedData.analyseData(Double.parseDouble(alphaValue.getText()), choiceColumn.getValue(), choiceC1.getValue(), choiceC2.getValue()));
            reportArea.setText(report.toString());
        } catch (RuntimeException e) {
            alertHandling(e.getMessage());
        }
    }

    @FXML
    public void saveDataToFile(){
        try {
            if (reportArea.getText().isEmpty())
                throw new RuntimeException("Brak danych do zapisu!");

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Zapisz dane");

            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki tekstowe (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(new Stage());
            saveTextToFile(report.toString(), file);
        } catch (RuntimeException e) {
            alertHandling(e.getMessage());
        }
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            alertHandling(ex.getMessage());
        }
    }

    @FXML
    public void drawHistogram()
    {
        try{
            if (analyzedData == null) {
                throw new RuntimeException("Nie wczytano pliku z danymi!");
            }
            BarChart<String,Number> bc = analyzedData.drawChart(choiceColumn.getValue());
            Stage histogramStage = new Stage();
            //new File(".\\images_chart\\").mkdirs();
            drawBase(bc, histogramStage, ".\\images_chart\\chart_");
        } catch (IOException e){
            alertHandling(e.getMessage());
        }
    }

    private void drawBase(BarChart<String, Number> bc, Stage histogramStage, String s) throws IOException {
        String nameData = Files.getNameWithoutExtension(inputFile.getName());
        histogramStage.setTitle("Histogram");
        bc.setTitle("Histogram dla zestawu danych: "+nameData);
        File imageFile = new File(s +nameData+".png");
        Files.createParentDirs(imageFile);
        Scene histogramScene = new Scene(bc,1280,720);
        WritableImage histogramImage = histogramScene.snapshot(null);
        ImageIO.write(SwingFXUtils.fromFXImage(histogramImage,null),"png", imageFile);
        histogramStage.setScene(histogramScene);
        histogramStage.show();
    }

    @FXML
    public void drawGroupedHistogram()
    {
        try{
            if (analyzedData == null) {
                throw new RuntimeException("Nie wczytano pliku z danymi!");
            }
            Stage histogramStage = new Stage();
            BarChart<String,Number> bc = analyzedData.drawGroupedChart(choiceColumn.getValue());
            drawBase(bc, histogramStage, "images_chart\\groupedChart_");
        } catch (IOException e){
            alertHandling(e.getMessage());
        }
    }

    @FXML
    public void alertHandling(String alertInfo) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Look, a Warning Dialog");
        alert.setContentText(alertInfo);
        alert.showAndWait();
    }

    @FXML
    public void quitApp() {
        Platform.exit();
    }
}
