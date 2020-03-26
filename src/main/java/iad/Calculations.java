package iad;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.commons.math3.special.Erf;
//import org.apache.commons.math3.stat.inference.TTest;

public class Calculations extends Data{
    public Calculations(File file, String separator) throws IOException {
        super(file, separator);
    }

    public StringBuilder analyseData(double alpha, String distributionType, int col, String classC1, String classC2)
    {
        StringBuilder report = new StringBuilder();
        report.append("Analizowa kolumna danych: ").append(col).append(System.lineSeparator()).append(System.lineSeparator());
        for(String obj : classTypes)
        {
            ArrayList<ArrayList<Double>> classData = data.get(obj);
            ArrayList<Double> columnData = classData.get(col);
            int decimalPlaces = 4;
            report.append("Wyniki dla klasy: ").append(obj).append(System.lineSeparator());
            report.append("1. Miary średnie klasyczne i pozycyjne:").append(System.lineSeparator());
            report.append("Średnia arytmetyczna: ").append(round(arithmeticAverage(columnData),decimalPlaces)).append(System.lineSeparator());
            report.append("Pierwszy kwartyl: ").append(round(quartile(1,columnData),decimalPlaces)).append(System.lineSeparator());
            report.append("Mediana: ").append(round(quartile(2,columnData),decimalPlaces)).append(System.lineSeparator());
            report.append("Trzeci kwartyl: ").append(round(quartile(3,columnData),decimalPlaces)).append(System.lineSeparator());

            report.append("2. Miary rozproszenia: ").append(System.lineSeparator());
            report.append("Wariancja: ").append(round(centralMoment(2, columnData),decimalPlaces)).append(System.lineSeparator());
            report.append("Odchylenie standardowe: ").append(round(standardDeviation(columnData),decimalPlaces)).append(System.lineSeparator());

            report.append("3. Miary asymetrii:").append(System.lineSeparator());
            report.append("Współczynnik asymetrii: ").append(round(skewnessCoefficient(columnData),decimalPlaces)).append(System.lineSeparator());
            report.append("Współczynnik skośności: ").append(round(skewness(columnData),decimalPlaces)).append(System.lineSeparator());

            report.append("4. Miary koncentracji:").append(System.lineSeparator());
            report.append("Współczynnik koncentracji(kurtoza): ").append(round(kurtosis(columnData),decimalPlaces)).append(System.lineSeparator());

            report.append("5. Normalizacja zmiennej losowej. Dopasowanie rozkładu i analiza danych w jego kontekście:").append(System.lineSeparator());
            report.append("Dane przed normalizacją:").append(System.lineSeparator());
            report.append(columnData).append(System.lineSeparator()).append(System.lineSeparator());
            report.append(counterOfDuplicate(columnData)).append(System.lineSeparator()).append(System.lineSeparator());
            report.append("Dane po normalizacji:").append(System.lineSeparator());
            //report.append(dataNormalization(columnData)).append(System.lineSeparator()).append(System.lineSeparator());
            report.append(Arrays.toString(dataNormalization(columnData).keySet().toArray())).append(System.lineSeparator()).append(System.lineSeparator());
            report.append(System.lineSeparator());
        }
        ArrayList<ArrayList<Double>> hiptestData1 = this.data.get(classC1);
        ArrayList<ArrayList<Double>> hiptestData2 = this.data.get(classC2);
        report.append(hypothesisTesting(hiptestData1.get(col),hiptestData2.get(col),alpha,distributionType));
        return report;
    }

    //średnia arytmetyczna
    private double arithmeticAverage(ArrayList<Double> inputData){
        double sum = 0.0D;
        double average;
        if(inputData.isEmpty()) {
            return sum;
        }

        for (double num : inputData){
            sum += num;
        }

        average = sum / inputData.size();
        return average;
    }

    //kwartyle
    private double quartile(int quartileIndex, ArrayList<Double> inputData){
        int numerator = quartileIndex * (inputData.size() - 1);
        int quotient = (int) Math.floor(numerator * 0.25);
        int isOdd = numerator - (quotient * 4);
        if (isOdd == 0) {
            return inputData.get(quotient);
        } else {
            return (inputData.get(quotient) + inputData.get(quotient + 1)) * 0.5;
        }
    }

    //moment centralny
    private double centralMoment(int power, ArrayList<Double> inputData) {
        double average = arithmeticAverage(inputData);
        double sum = 0.0D;
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

    //odchylenie standardowe z próby
    private double standardDeviationFromSample(ArrayList<Double> inputData) {
        double average = arithmeticAverage(inputData);
        double sum = 0.0D;
        for (double num : inputData){
            double sub = num - average;
            sum += Math.pow(sub, 2);
        }
        return Math.sqrt(sum/(inputData.size()-1.0D));
    }

    //współczynnik skośności(“Klasyczno-pozycyjny” współczynnik skośności (miara absolutna))
    private double skewness(ArrayList<Double> inputData) {
        return 3*((arithmeticAverage(inputData) - quartile(2,inputData)) / standardDeviation(inputData));
    }

    //współczynnik asymetrii (Klasyczny współczynnik asymetrii)
    private double skewnessCoefficient(ArrayList<Double> inputData){
        return centralMoment(3, inputData) / Math.pow(standardDeviation(inputData), 3);
    }

    /* współczynnik kurtozy */
    private double kurtosis(ArrayList<Double> inputData) {
        return (centralMoment(4, inputData) / Math.pow(standardDeviation(inputData), 4)) - 3;
    }

    protected SortedMap<Double, Long> counterOfDuplicate(ArrayList<Double> inputData) {
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

    double round(double value, int decimalPlaces) {
        double pow = Math.pow(10.0, decimalPlaces);
        return Math.round(value * pow) / pow;
    }

    private StringBuilder hypothesisTesting(ArrayList<Double> x1, ArrayList<Double> x2, double alpha, String distributionType)
    {
        StringBuilder result = new StringBuilder();
        double m1 = arithmeticAverage(x1);
        double m2 = arithmeticAverage(x2);
        double s1 = standardDeviationFromSample(x1);
        double s2 = standardDeviationFromSample(x2);

        double val1 = (s1*s1) / x1.size();
        double val2 = (s2*s2) / x2.size();

        double z = (m1 - m2) / Math.sqrt(val1 + val2);
        int decimalPlaces = 4;
        result.append("m1 = ").append(round(m1,decimalPlaces)).append(System.lineSeparator());
        result.append("m2 = ").append(round(m2,decimalPlaces)).append(System.lineSeparator());
        result.append("s1 = ").append(round(s1,decimalPlaces)).append(System.lineSeparator());
        result.append("s2 = ").append(round(s2,decimalPlaces)).append(System.lineSeparator());
        result.append("z = ").append(round(z,decimalPlaces)).append(System.lineSeparator());

        double df = (val1 + val2) * (val1 + val2)
                / (((val1*val1) / (x1.size() - 1.0D))
                + ((val2*val2) / (x2.size() - 1.0D)));

        double p = (1 - Math.abs(Erf.erf(-z / Math.sqrt(2))));
        if(distributionType.equals("jednostronny")) {
            p /=2;
        }
        /*TTest test = new TTest();
        double[] x1arr = x1.stream().mapToDouble(Double::doubleValue).toArray();
        double[] x2arr = x2.stream().mapToDouble(Double::doubleValue).toArray();
        double p = test.tTest(x1arr,x2arr);
        boolean rejectH = test.tTest(x1arr,x2arr,alpha);*/

        result.append("Rozkład t-Studenta = ").append(round(df,decimalPlaces)).append(System.lineSeparator());
        result.append("p-value = ").append(round(p,6)).append(System.lineSeparator());
        //result.append("p-value rejec = ").append(rejectH).append(System.lineSeparator());
        if (p <= alpha)
            result.append("Odrzucamy hipotezę zerową H0 dla rozkładu normalnego. (p-value <= ")
                    .append(alpha).append( ")").append(System.lineSeparator());
        else
            result.append("Nie odrzucamy hipotezy zerowej H0 dla rozkładu normalnego. (p-value > ")
                    .append(alpha).append( ")").append(System.lineSeparator());

        return result;
    }
}
