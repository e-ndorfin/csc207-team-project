package com.sketchandguess.api;

public class APIHandler {
    private String inferenceUrl;

    public APIHandler(String inferenceUrl) {
        this.inferenceUrl = inferenceUrl;
    }

    public String getInferenceUrl() {
        return inferenceUrl;
    }

    public void setInferenceUrl(String inferenceUrl) {
        this.inferenceUrl = inferenceUrl;
    }
}