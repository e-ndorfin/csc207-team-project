package com.sketchandguess.usecases.editsettings;

/**
 * Data returned from Use Case → Presenter for EditSettings.
 * Contains success status, error message (if failed), and the updated settings.
 */
public class EditSettingsOutputData {
    public final boolean success;
    public final String errorMessage;
    public final double defaultTimeLimit;
    public final String difficultyName;

    public EditSettingsOutputData(boolean success, String errorMessage, double defaultTimeLimit, String difficultyName) {
        this.success = success;
        this.errorMessage = errorMessage != null ? errorMessage : "";
        this.defaultTimeLimit = defaultTimeLimit;
        this.difficultyName = difficultyName != null ? difficultyName.toLowerCase() : "medium";
    }
    
    // Convenient error function 
    public boolean hasError() {
        return !success || !errorMessage.isEmpty();
    }
}
