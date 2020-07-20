package main.models;

public class TrainItem {

    private double label;
    private double[] pixels;

    public void setLabel(String label) {
        this.label = Double.parseDouble(label);
    }

    public void setPixels(String[] pixels) {
        this.pixels = new double[pixels.length];
        for (int arrIdx = 0; arrIdx < pixels.length; arrIdx++)
            this.pixels[arrIdx] = Double.parseDouble(pixels[arrIdx]);
    }

    public double getLabel() {
        return label;
    }

    public double[] getPixels() {
        return pixels;
    }

    public void normalizePixels(double maxValue) {
        for (int idx = 0; idx < pixels.length; idx++) {
            pixels[idx] = pixels[idx] / maxValue;
        }
    }
}
