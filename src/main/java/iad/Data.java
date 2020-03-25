package iad;

import java.io.*;
import java.util.*;

public class Data {
    private File file;
    private String separator;
    protected HashMap<String,ArrayList<ArrayList<Double>>> data;
    protected TreeSet<String> classTypes;
    private int numberOfAttribute;

    public Data(File file, String separator) throws IOException {
        this.file = file;
        this.separator = separator;
        getClassTypeFromFile();
        getDataOfFile();
    }

    public String getSeparator() {
        return separator;
    }

    public int getNumberOfAttribute() {
        return numberOfAttribute;
    }

    public TreeSet<String> getClassTypes() {
        return classTypes;
    }

    private void getClassTypeFromFile() throws IOException {
        classTypes = new TreeSet<>();
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String currentLine = buffer.readLine();
        String[] separatedDataLine = currentLine.split(separator);
        classTypes.add(separatedDataLine[separatedDataLine.length - 1]);
        numberOfAttribute = separatedDataLine.length;

        while((currentLine = buffer.readLine()) != null) {
            separatedDataLine = currentLine.split(separator);
            classTypes.add(separatedDataLine[separatedDataLine.length - 1]);
        }
    }

    private void getDataOfFile() throws IOException {
        data = new HashMap<>();
        for(String obj : classTypes) {
            data.put(obj, getDataOfColumns(obj));
        }
    }


    private ArrayList<ArrayList<Double>> getDataOfColumns(String classType) throws IOException {
        BufferedReader buffer;
        ArrayList<ArrayList<Double>> columnsData = new ArrayList<>();
        for (int i = 0; i < numberOfAttribute - 1; i++) {
            columnsData.add(new ArrayList<>());
        }

        buffer = new BufferedReader(new FileReader(file));

        String currentLine;
        while((currentLine = buffer.readLine()) != null) {
            String[] separatedDataLine = currentLine.split(separator);
            if ((separatedDataLine[separatedDataLine.length - 1]).equals(classType)) {
                for (int i = 0; i < separatedDataLine.length-1; i++) {
                    columnsData.get(i).add(Double.parseDouble(separatedDataLine[i]));
                }
            }
        }

        for (ArrayList<Double> columnData : columnsData) {
            Collections.sort(columnData);
        }
        return columnsData;
    }
}
