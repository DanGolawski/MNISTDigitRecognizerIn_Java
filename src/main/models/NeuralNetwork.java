package main.models;

import java.util.List;
import java.util.stream.IntStream;

public class NeuralNetwork {

    private Neuron[][] layers;
    private double beta;
    private double eta;

    public NeuralNetwork(int inputSize, int[] hidLayers, int outputSize, double beta, double eta) {
        this.beta = beta;
        this.eta = eta;
        int layerIdx = 0;
        // length -> input layer + hidden layers + output layer
        layers = new Neuron[hidLayers.length+2][];
        // initializing of input layer
        layers[layerIdx++] = initializeLayer(inputSize, 0);
        // initializing of hidden layers
        for (int layerSize : hidLayers) {
            int prevIdx = layerIdx - 1;
            layers[layerIdx++] = initializeLayer(layerSize, layers[prevIdx].length);
        }
        // initializing of output layer
        layers[layerIdx] = initializeLayer(outputSize, layers[layerIdx-1].length);
    }

    private Neuron[] initializeLayer(int neuronsNumber, int inputsNumber) {
        Neuron[] neurons = new Neuron[neuronsNumber];
        IntStream.range(0, neuronsNumber).forEach(idx ->
                neurons[idx] = new Neuron(inputsNumber, beta, eta));
        return neurons;
    }

    public void train(List<TrainItem> trainingData, double tollerance) {
        int trainProgress = 0;
        int target = trainingData.size();

        while (trainProgress < target) {
            TrainItem currItem = trainingData.get(trainProgress);
            double expected = currItem.getLabel() / 10;
            double result = runCalculation(currItem.getPixels());
            double loss = (expected - result) / expected;
            System.out.println(trainProgress + " ---- " + result + " ----- " + expected);
            if (Math.abs(loss) > tollerance) {
                correctWeights(loss);
                trainProgress = 0;
            }
            else {
                trainProgress++;

            }
        }
    }

    public int predict(double[] pixels) {
        double result = runCalculation(pixels);
        return (int) Math.round(result * 10);
    }

    private double runCalculation(double[] pixels) {
        int pixelIdx = 0;
        // setting values for input layer
        for (double pixel : pixels)
            layers[0][pixelIdx++].setOutput(pixel);
        // calculation within hidden layers neurons and output layer neurons
        IntStream.range(1, layers.length).forEach(this::stimulateNeurons);
        return layers[layers.length-1][0].getOutput();
    }

    private void stimulateNeurons(int index) {
        // get neuron outputs of previous layer
        int prevLayerSize = layers[index-1].length;
        double[] prevLyrValues = new double[prevLayerSize];
        int neuronIdx = 0;
        for (Neuron prevNeuron : layers[index - 1])
            prevLyrValues[neuronIdx++] = prevNeuron.getOutput();
        // run the calculations for each neuron within current layer
        for (Neuron neuron : layers[index]) {
            neuron.runCalculations(prevLyrValues);
        }
    }

    private void correctWeights(double loss) {
        // set the loss to the output neuron
        layers[layers.length-1][0].setLoss(loss);
        // set losses for neurons within each hidden layer
        for (int currLayerIdx = layers.length-2; currLayerIdx > 0; currLayerIdx--) {
            correctWeightsWithinLayer(currLayerIdx);
        }
    }

    private void correctWeightsWithinLayer(int currLayerIdx) {
        for (int neuronIdx = 0; neuronIdx < layers[currLayerIdx].length; neuronIdx++) {
            double loss = calculateLossForNeuron(currLayerIdx + 1, neuronIdx);
            layers[currLayerIdx][neuronIdx].setLoss(loss);
            double[] inputValuesForNeuron = getInputValuesForNeuron(currLayerIdx);
            layers[currLayerIdx][neuronIdx].correctWeights(inputValuesForNeuron);
        }

    }

    private double calculateLossForNeuron(int nextLayerIdx, int neuronIdx) {
        double sum = 0;
        for (Neuron neuron : layers[nextLayerIdx]) {
            sum += neuron.getWeightLossProduct(neuronIdx);
        }
        return sum;
    }

    private double[] getInputValuesForNeuron(int layerIndex) {
        double[] inputValues = new double[layers[layerIndex-1].length];
        int valueIdx = 0;
        for (Neuron neuron : layers[layerIndex-1]) {
            inputValues[valueIdx++] = neuron.getOutput();
        }
        return inputValues;
    }
}
