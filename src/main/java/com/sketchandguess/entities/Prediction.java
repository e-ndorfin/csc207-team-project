package com.sketchandguess.entities;

/**
 * Represents a single prediction with its label and confidence score.
 */
public class Prediction {
    private final String label;
    private final double confidence;

    public Prediction(String label, double confidence) {
        this.label = label;
        this.confidence = confidence;
    }

    public String getLabel() {
        return label;
    }

    public double getConfidence() {
        return confidence;
    }

    /**
     * Returns confidence as a percentage (0-100).
     */
    public double getConfidencePercent() {
        return confidence * 100.0;
    }
}

