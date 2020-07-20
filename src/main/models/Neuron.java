package main.models;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Neuron {

    private double[] weights;
    private double output;
    private double beta;
    private double eta;
    private double loss;

    public Neuron(int inputsNumber, double beta, double eta) {
        this.beta = beta;
        this.eta = eta;
        weights = new double[inputsNumber + 1];
        Random rand = new Random();
        IntStream.range(0, inputsNumber).forEach(idx -> weights[idx] = rand.nextDouble() * 10);
    }

    public void setOutput(double value) {
        output = value;
    }

    public double getOutput() {
        return output;
    }

    public void setLoss(double value) {
        loss = value;
    }

    public double getLoss() {
        return loss;
    }

    public void runCalculations(double[] inputValues) {
        double[] values = extendArray(inputValues);
        for (int idx = 0; idx < values.length; idx++)
            values[idx] = values[idx] * weights[idx];
        double sum = sum(values);
        output = 1 / (1 + Math.exp(-beta * sum));
    }

    private double[] extendArray(double[] array) {
        int arraySize = array.length;
        array = Arrays.copyOf(array, arraySize + 1);
        array[arraySize] = 1;
        return array;
    }

    private double sum(double[] values) {
        double sum = 0;
        for (double val : values)
            sum += val;
        return sum;
    }

    public double getWeightLossProduct(int weightIdx) {
        return weights[weightIdx] * loss;
    }

    public void correctWeights(double[] inputValuesForNeuron) {
        double[] values = extendArray(inputValuesForNeuron);
        for (int weightIdx = 0; weightIdx < weights.length; weightIdx++) {
            weights[weightIdx] = weights[weightIdx] + (eta
                    * loss
                    * (1 - output)
                    * values[weightIdx]
            );
        }
    }

}
