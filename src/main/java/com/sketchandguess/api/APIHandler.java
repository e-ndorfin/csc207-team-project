package com.sketchandguess.api;

public class APIHandler {
    private String apiKey;
    private String inferenceUrl;
    private double confidenceThreshold;
    private int maxGuessesToReturn;

    public APIHandler(String apiKey, String inferenceUrl, double confidenceThreshold, int maxGuessesToReturn) {
        this.apiKey = apiKey;
        this.inferenceUrl = inferenceUrl;
        this.confidenceThreshold = confidenceThreshold;
        this.maxGuessesToReturn = maxGuessesToReturn;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getInferenceUrl() {
        return inferenceUrl;
    }

    public void setInferenceUrl(String inferenceUrl) {
        this.inferenceUrl = inferenceUrl;
    }

    public double getConfidenceThreshold() {
        return confidenceThreshold;
    }

    public void setConfidenceThreshold(double confidenceThreshold) {
        this.confidenceThreshold = confidenceThreshold;
    }

    public int getMaxGuessesToReturn() {
        return maxGuessesToReturn;
    }

    public void setMaxGuessesToReturn(int maxGuessesToReturn) {
        this.maxGuessesToReturn = maxGuessesToReturn;
    }
}
