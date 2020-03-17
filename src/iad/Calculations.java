package iad;

import java.io.File;
import java.util.*;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.inference.TTest;

public class Calculations {
    private TreeSet<String> classType;
    private HashMap<String,ArrayList<ArrayList<Double>>> data;
    public Calculations(HashMap<String,ArrayList<ArrayList<Double>>> data, TreeSet<String> classTypes) {
        this.classType = classTypes;
        this.data = data;
        ArrayList<ArrayList<Double>> temp0 = this.data.get("Iris-setosa");
        ArrayList<ArrayList<Double>> temp1 = this.data.get("Iris-virginica");
        System.out.println(hypothesisTesting(temp0.get(1),temp1.get(1),0.05));
        /*for(String obj : classTypes)
        {
            ArrayList<ArrayList<Double>> temp = this.data.get(obj);
            //temp.get(0);
            //String temp2 = this.data.get(obj);
            ArrayList<Double> temp2 = temp.get(0);
//            System.out.println(quartile(2,temp2));
            //System.out.println(round(kurtosis(temp2),4));
            //System.out.println(dataNormalization(temp2));
            System.out.println();

        }*/

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

    /* współczynnik kurtozy */
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

    private StringBuilder hypothesisTesting(ArrayList<Double> x1, ArrayList<Double> x2, double alpha)
    {
        StringBuilder result = new StringBuilder();
        double m1 = arithmeticAverage(x1);
        double m2 = arithmeticAverage(x2);
        double s1 = standardDeviation(x1);
        double s2 = standardDeviation(x2);

        double val1 = Math.pow(s1, 2) / x1.size();
        double val2 = Math.pow(s2, 2) / x2.size();

        double z = (m1 - m2) / Math.sqrt(val1 + val2);

        result.append("m1 = ").append(round(m1,4)).append(System.lineSeparator());
        result.append("m2 = ").append(round(m2,4)).append(System.lineSeparator());
        result.append("s1 = ").append(round(s1,4)).append(System.lineSeparator());
        result.append("s2 = ").append(round(s2,4)).append(System.lineSeparator());
        result.append("z = ").append(round(z,4)).append(System.lineSeparator());

        double df = Math.pow((val1 + val2), 2)
                / ((Math.pow(val1, 2) / (x1.size() - 1))
                + (Math.pow(val2, 2) / (x2.size() - 1)));

        double p = 1 + Erf.erf(-z / Math.sqrt(2));
       /* TTest test = new TTest();
        double[] x1arr = x1.stream().mapToDouble(Double::doubleValue).toArray();
        double[] x2arr = x2.stream().mapToDouble(Double::doubleValue).toArray();
        double palt = test.tTest(x1arr,x2arr);*/

        result.append("Rozkład t-Studenta = ").append(round(df,4)).append(System.lineSeparator());
        result.append("p-value = ").append(p).append(System.lineSeparator());
        if (p <= alpha)
            result.append("Odrzucamy hipotezę zerową H0 dla rozkładu normalnego. (p-value <= ")
                    .append(alpha).append( ")").append(System.lineSeparator());
        else
            result.append("Nie odrzucamy hipotezy zerowej H0 dla rozkładu normalnego. (p-value > ")
                    .append(alpha).append( ")").append(System.lineSeparator());

        return result;
    }
}
