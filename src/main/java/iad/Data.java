package iad;

//import com.sun.source.tree.Tree;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Data {
    private File file;
    private String separator;
    private HashMap<String,ArrayList<ArrayList<Double>>> data;
    private TreeSet<String> classTypes;
    private int numberOfAttribute;

    public String getSeparator() {
        return separator;
    }

    public int getNumberOfAttribute() {
        return numberOfAttribute;
    }

    public HashMap<String,ArrayList<ArrayList<Double>>> getData() {
        return data;
    }

    public TreeSet<String> getClassTypes() {
        TreeSet cloned_set = (TreeSet)classTypes.clone();
        return cloned_set;
    }

    public Data(File file, String separator) {
        this.file = file;
        this.separator = separator;
        getClassType();
        getDataOfFile();
       // System.out.println(getDataOfColumns("Iris-setosa"));
        //arithmeticAverage("Iris-setosa");
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
                data.put(obj.toString(),getDataOfColumns(obj));
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

    /*private void arithmeticAverage(String classType) {

        ArrayList<ArrayList<Double>> columnsData = getDataOfColumns(classType);
        double tab[] = new double[columnsData.size()];
        System.out.println(tab.length);
         for(int i=0;i<columnsData.size();i++) {
             for(int j=0;j<columnsData.get(i).size();j++) {
                 tab[i] += columnsData.get(i).get(j);
             }
             tab[i] /= Math.round(columnsData.get(i).size()*1000.0)/1000.0;
         }

        System.out.println("Srednia = " + tab[0]);
        //System.out.println("ilosc lini: " + number);
    }*/
}
