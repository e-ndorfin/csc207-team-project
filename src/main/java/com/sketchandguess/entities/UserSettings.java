package com.sketchandguess.entities;

public class UserSettings {
    private String userId;
    private double defaultTimeLimit;
    private String defaultExportFormat;

    // chosen difficulty: "easy", "medium", "hard"
    private String difficultyName;

    public UserSettings(String userId, double defaultTimeLimit, String defaultExportFormat) {
        this.userId = userId;
        this.defaultTimeLimit = defaultTimeLimit;
        this.defaultExportFormat = defaultExportFormat;
        this.difficultyName = "medium"; // default
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getDefaultTimeLimit() {
        return defaultTimeLimit;
    }

    public void setDefaultTimeLimit(double defaultTimeLimit) {
        this.defaultTimeLimit = defaultTimeLimit;
    }

    public String getDefaultExportFormat() {
        return defaultExportFormat;
    }

    public void setDefaultExportFormat(String defaultExportFormat) {
        this.defaultExportFormat = defaultExportFormat;
    }

    public String getDifficultyName() {
        return difficultyName;
    }

    public void setDifficultyName(String difficultyName) {
        this.difficultyName = difficultyName;
    }
}
