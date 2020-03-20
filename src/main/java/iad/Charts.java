package iad;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Charts {
    private Calculations calc;
    private Data data;
    public Charts(Calculations calc, Data data) {
        this.calc = calc;
        this.data = data;
    }

    public BarChart<String,Number> drawChart(int col) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Histogram");
        xAxis.setLabel("Wartość");
        yAxis.setLabel("Ilość");
        for (String obj : data.getClassTypes()) {
            ArrayList<ArrayList<Double>> classData = data.getData().get(obj);
            ArrayList<Double> columnData = classData.get(col);
            Map<Double,Long> countedData = calc.counterOfDuplicate(columnData);
            bc.getData().add(createDataSeries(countedData, obj));
        }

        return bc;
    }

    public BarChart<String,Number> drawGroupedChart(int col) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Histogram");
        xAxis.setLabel("Wartość");
        yAxis.setLabel("Ilość");
        TreeMap<Double, Long> groupedDataMap;
        ArrayList<Double> allDataFromClass = new ArrayList<Double>();
        for (String obj : data.getClassTypes()) {
            ArrayList<ArrayList<Double>> classData = data.getData().get(obj);
            ArrayList<Double> columnData = classData.get(col);
            allDataFromClass.addAll(columnData);
        }
        groupedDataMap = createRangeMap(calc.counterOfDuplicate(allDataFromClass));
        for (String obj : data.getClassTypes()) {
            ArrayList<ArrayList<Double>> classData = data.getData().get(obj);
            ArrayList<Double> columnData = classData.get(col);
            Map<Double,Long> countedData = calc.counterOfDuplicate(columnData);
            groupData(groupedDataMap,countedData);
            bc.getData().add(createDataSeries(groupedDataMap, obj));
        }
        return bc;
    }


    private void groupData(TreeMap<Double, Long> groupedData, Map<Double,Long> data)
    {
        for(Map.Entry<Double,Long> value : data.entrySet()) {
            double lowerLimit = groupedData.firstKey();
            for(Map.Entry<Double,Long> valueGroupedData: groupedData.entrySet()) {

                if(lowerLimit <= value.getKey() && valueGroupedData.getKey() >= value.getKey())
                {
                    //int count = valueGroupedData.getValue() + 1;
                    //count++;
                    groupedData.put(valueGroupedData.getKey(), valueGroupedData.getValue() + 1);
                }

                lowerLimit = valueGroupedData.getKey();

            }

        }
    }

    private TreeMap<Double,Long> createRangeMap(Map<Double,Long> data) {
        TreeMap<Double,Long> groupedData = new TreeMap<Double, Long>();
        int k = (int) Math.sqrt(data.size());
        double min = (double) data.keySet().toArray()[0];
        double max = (double) data.keySet().toArray()[data.size() - 1];
        double gap = (max - min) / k;
        double lowerLimit = min - (gap / 2);

        for (int i = 0; i < k; i++) {
            groupedData.put(calc.round(lowerLimit,3), 0L);
            lowerLimit += gap;
        }
        return groupedData;
    }

    private XYChart.Series<String,Number> createDataSeries(Map<Double,Long> data, String nameSeries) {
        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
        series.setName(nameSeries);
        for(Map.Entry<Double,Long> obj: data.entrySet())
        {
            series.getData().add(new XYChart.Data<>(obj.getKey().toString(),obj.getValue()));
        }
        return series;
    }
}