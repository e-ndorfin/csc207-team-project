package com.sketchandguess.interface_adapters.settings;

public class SettingsState {
    private double defaultTimeLimit;
    private String difficultyName;
    private String successMessage;
    private String errorMessage;

    // Default constructor
    public SettingsState() {
        this.defaultTimeLimit = 30.0;
        this.difficultyName = "medium";
        this.successMessage = "";
        this.errorMessage = "";
    }

    public SettingsState(double defaultTimeLimit, String difficultyName) {
        this.defaultTimeLimit = defaultTimeLimit;
        this.difficultyName = difficultyName != null ? difficultyName.toLowerCase() : "medium";
        this.successMessage = "";
        this.errorMessage = "";
    }

    // Getters and Setters
    public double getDefaultTimeLimit() {
        return defaultTimeLimit;
    }

    public void setDefaultTimeLimit(double defaultTimeLimit) {
        this.defaultTimeLimit = defaultTimeLimit;
    }

    public String getDifficultyName() {
        return difficultyName;
    }

    public void setDifficultyName(String difficultyName) {
        this.difficultyName = difficultyName != null ? difficultyName.toLowerCase() : "medium";
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage != null ? successMessage : "";
        // Only clear error if we're setting a non-empty success message
        if (!this.successMessage.isEmpty()) {
            this.errorMessage = "";
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage != null ? errorMessage : "";
        
        // Check for empty error
        if (!this.errorMessage.isEmpty()) {
            this.successMessage = "";
        }
    }

    public String getMessage() {
        return successMessage.isEmpty() ? errorMessage : successMessage;
    }
}
