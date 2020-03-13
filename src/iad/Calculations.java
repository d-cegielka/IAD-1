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
            System.out.println(standardDeviation(temp2));
        }

    }

    private double arithmeticAverage(ArrayList<Double> inputData){
        double sum = 0;
        double average;
        if(inputData.isEmpty())
            return sum;
        for (double num : inputData)
            sum += num;

        average = Math.round(sum / inputData.size()*1000.0)/1000.0;

        return average;
    }

    private double quartile(int quartileIndex, ArrayList<Double> inputData){
        return inputData.get((int) Math.round((inputData.size() * (quartileIndex * 0.25))));
    }

    private double centralMoment(int power, ArrayList<Double> inputData) {
        double average = arithmeticAverage(inputData);
        double sum = 0;
        for (double num : inputData){
            double sub = num - average;
            sum += Math.pow(sub, power);
        }
        return Math.round(sum/inputData.size()*10000d)/10000d;
    }

    private double standardDeviation(ArrayList<Double> inputData) {
        double standardDeviation = Math.sqrt(centralMoment(2,inputData));
        return Math.round(standardDeviation*10000d)/10000d;
        //TODO zwracać pełną wartość w funkcjach, przy wypisywaniu zaokrąglić.
    }
}
