package main.services;

import main.models.TrainItem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataService {

    public List<TrainItem> readData(String filename) {
        String csvFile = "src/main/resources/" + filename;
        BufferedReader br = null;
        String line = "";
        String separator = ",";
        List<TrainItem> trainItems = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine(); //skips the first row
            while ((line = br.readLine()) != null) {
                String[] data = line.split(separator);
                trainItems.add(createItem(data));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return trainItems;
    }

    private TrainItem createItem(String[] data) {
        TrainItem item = new TrainItem();
        // label
        item.setLabel(data[0]);
        // pixels
        String[] pixels = Arrays.copyOfRange(data, 1, data.length);
        item.setPixels(pixels);
        return item;
    }

    public List<TrainItem> normalize(List<TrainItem> items) {
        double maxValue = -1;
        // find max value to normalize;
        for (TrainItem item : items) {
            double[] data = item.getPixels();
            double currMaxValue = findMaxValue(data);
            maxValue = currMaxValue > maxValue ? currMaxValue : maxValue;
        }
        for (TrainItem item : items) {
            item.normalizePixels(maxValue);
        }
        return items;
    }

    private double findMaxValue(double[] data) {
        double maxVal = -1;
        for (double val : data) {
            if (val > maxVal)
                maxVal = val;
        }
        return maxVal;
    }
}
