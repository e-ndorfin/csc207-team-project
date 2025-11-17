package com.sketchandguess.entities;

public class UserSettings {
    private String userId;
    private double defaultTimeLimit;
    private String defaultExportFormat;

    public UserSettings(String userId, double defaultTimeLimit, String defaultExportFormat) {
        this.userId = userId;
        this.defaultTimeLimit = defaultTimeLimit;
        this.defaultExportFormat = defaultExportFormat;
    }

    public double getDefaultTimeLimit() {
        return defaultTimeLimit;
    }

    public void setDefaultTimeLimit(double defaultTimeLimit) {
        this.defaultTimeLimit = defaultTimeLimit;
    }

    // You can add other getters/setters when you need them
}
