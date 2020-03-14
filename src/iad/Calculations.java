package iad;

import java.io.File;
import java.util.*;

public class Calculations {
    private TreeSet<String> classType;
    private HashMap<String,ArrayList<ArrayList<Double>>> data;
    public Calculations(HashMap<String,ArrayList<ArrayList<Double>>> data, TreeSet<String> classTypes) {
        this.classType = classTypes;
        this.data = data;
        for(String obj : classTypes)
        {
            ArrayList<ArrayList<Double>> temp = this.data.get(obj);
            //temp.get(0);
            //String temp2 = this.data.get(obj);
            ArrayList<Double> temp2 = temp.get(0);
//            System.out.println(quartile(2,temp2));
            //System.out.println(round(kurtosis(temp2),4));
            System.out.println(dataNormalization(temp2));
        }

    }

    //średnia arytmetyczna
    private double arithmeticAverage(ArrayList<Double> inputData){
        double sum = 0;
        double average;
        if(inputData.isEmpty())
            return sum;
        for (double num : inputData)
            sum += num;

        average = sum / inputData.size();
        return average;
    }

    //kwartyle
    private double quartile(int quartileIndex, ArrayList<Double> inputData){
        return inputData.get((int) Math.round((inputData.size() * (quartileIndex * 0.25))));
    }

    //moment centraly
    private double centralMoment(int power, ArrayList<Double> inputData) {
        double average = arithmeticAverage(inputData);
        double sum = 0;
        for (double num : inputData){
            double sub = num - average;
            sum += Math.pow(sub, power);
        }
        return sum/inputData.size();
    }

    //odchylenie standardowe
    private double standardDeviation(ArrayList<Double> inputData) {
        return Math.sqrt(centralMoment(2,inputData));
    }

    //współczynnik skośności
    private double skewness(ArrayList<Double> inputData) {
        return (arithmeticAverage(inputData) - quartile(2,inputData)) / standardDeviation(inputData);
    }

    //współczynnik asymetrii
    private double skewnessCoefficient(ArrayList<Double> inputData){
        return centralMoment(3,inputData) / standardDeviation(inputData);
    }

    //współczynnik kurtozy
    private double kurtosis(ArrayList<Double> inputData) {
        return centralMoment(4,inputData) / Math.pow(standardDeviation(inputData),4);
    }

    private SortedMap<Double, Long> counterOfDuplicate(ArrayList<Double> inputData) {
        SortedMap<Double, Long> resultMap = new TreeMap<>();
        for(Double obj : inputData) {
            if(resultMap.containsKey(obj)) {
                resultMap.put(obj,resultMap.get(obj) + 1L);
            } else {
                resultMap.put(obj,1L);
            }
        }
        return resultMap;
    }

    private SortedMap<Double, Long> dataNormalization(ArrayList<Double> inputData) {
        SortedMap<Double, Long> dataBeforeNormalization = counterOfDuplicate(inputData);
        SortedMap<Double, Long> dataAfterNormalization = new TreeMap<>();

        for (double key : dataBeforeNormalization.keySet()) {
            double value;
            value = (key - arithmeticAverage(inputData)) / standardDeviation(inputData);
            dataAfterNormalization.put(round(value,3),dataBeforeNormalization.get(key));
        }
        return dataAfterNormalization;
    }

    private double round(double value, int decimalPlaces) {
        double pow = Math.pow(10.0, decimalPlaces);
        return Math.round(value * pow) / pow;
    }
}
