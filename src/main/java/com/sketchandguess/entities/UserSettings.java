package com.sketchandguess.entities;

public class UserSettings {
    private String userId;
    private double defaultTimeLimit;
    private String defaultExportFormat;

    // NEW: store the chosen difficulty for this user
    // expected values: "easy", "medium", "hard"
    private String difficultyName;

    public UserSettings(String userId, double defaultTimeLimit, String defaultExportFormat) {
        this.userId = userId;
        this.defaultTimeLimit = defaultTimeLimit;
        this.defaultExportFormat = defaultExportFormat;

        // default difficulty for new users
        this.difficultyName = "medium";
    }

    public double getDefaultTimeLimit() {
        return defaultTimeLimit;
    }

    public void setDefaultTimeLimit(double defaultTimeLimit) {
        this.defaultTimeLimit = defaultTimeLimit;
    }

    // NEW: getter/setter used by Settings + EditSettingsUseCase
    public String getDifficultyName() {
        return difficultyName;
    }

    public void setDifficultyName(String difficultyName) {
        this.difficultyName = difficultyName;
    }

    // You can add other getters/setters when you need them
}
