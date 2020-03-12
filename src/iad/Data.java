package iad;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Data {
    private File file;
    private String separator;
    public HashMap<ArrayList<ArrayList<Double>>, String> data;
    public SortedSet<String> classTypes;
    public int numberOfAttribute;

    public Data(File file, String separator) {
        this.file = file;
        this.separator = separator;
        getClassType();
        getDataOfFile();
    }

    private void getClassType() {
        String currentLine;
        String[] separatedDataLine;
        classTypes = new TreeSet<String>();

        try {
            BufferedReader buffor = new BufferedReader(new FileReader(file));
            currentLine = buffor.readLine();
            separatedDataLine = currentLine.split(separator);
            classTypes.add(separatedDataLine[separatedDataLine.length - 1]);
            numberOfAttribute = separatedDataLine.length;

            while((currentLine = buffor.readLine()) != null) {
                separatedDataLine = currentLine.split(separator);
                classTypes.add(separatedDataLine[separatedDataLine.length - 1]);
            }
        } catch (IOException e) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null,e);
        }
    }

    private void getDataOfFile() {
        data = new HashMap<>();
        for(String obj : classTypes)
        {
                data.put(getDataOfColumns(obj), obj.toString());
        }
        //System.out.println("Dane wczytane poprawnie!");
    }


    private ArrayList<ArrayList<Double>> getDataOfColumns(String classType) {
        BufferedReader buffor;
        String currentLine;
        String[] separatedDataLine;
        ArrayList<ArrayList<Double>> columnsData = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < numberOfAttribute - 1; i++) {
            columnsData.add(new ArrayList<Double>());
        }

        try {
            buffor = new BufferedReader(new FileReader(file));

            while((currentLine = buffor.readLine()) != null) {
                separatedDataLine = currentLine.split(separator);
                if ((separatedDataLine[separatedDataLine.length - 1]).equals(classType)) {
                    for (int i = 0; i < separatedDataLine.length-1; i++) {
                        columnsData.get(i).add(Double.parseDouble(separatedDataLine[i]));
                    }
                }
            }
        }
        catch (IOException e) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null,e);
        }

        for (int i = 0; i < columnsData.size(); i++) {
            Collections.sort(columnsData.get(i));
        }
        return columnsData;
    }
}
