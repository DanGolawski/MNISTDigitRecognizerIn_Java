package main;

import main.models.NeuralNetwork;
import main.models.TrainItem;
import main.services.DataService;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        DataService dataService = new DataService();

        // training data
        List<TrainItem> trainItems = dataService.readData("train.csv");
        trainItems = dataService.normalize(trainItems);
//        TrainItem itemForTest = new TrainItem();
//        itemForTest.setLabel("6");
//        itemForTest.setPixels(new String[]{"0", "1", "2", "0"});
//        List<TrainItem> trainItems = new ArrayList<>();
//        trainItems.add(itemForTest);
//        trainItems = dataService.normalize(trainItems);

        // initializing of neural network
        int inputLayerSize = trainItems.get(0).getPixels().length;
        int[] hiddenLayersSizes = { 3, 2 };
        double beta = 0.9;
        double eta = 0.6;
        NeuralNetwork neuralNetwork = new NeuralNetwork(inputLayerSize, hiddenLayersSizes, 1, beta, eta);

        // training the network
        neuralNetwork.train(trainItems.subList(0, 41000), 0.1);

        // testing the network
        int result = 0;
        for (TrainItem item : trainItems.subList(41001, 42000)) {
            int r = neuralNetwork.predict(item.getPixels());
            System.out.println(r + " ------- " + item.getLabel());
            if (r == item.getLabel())
                result++;
        }
        System.out.println(result);

    }
}
