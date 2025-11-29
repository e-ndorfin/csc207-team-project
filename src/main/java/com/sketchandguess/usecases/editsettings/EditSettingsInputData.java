package com.sketchandguess.usecases.editsettings;

/**
 * Data passed from Controller → Use Case for editing settings.
 * Contains the new time limit and difficulty to set.
 */
public class EditSettingsInputData {
    public final int timeLimitInSeconds;
    public final String difficultyName;

    public EditSettingsInputData(int timeLimitInSeconds, String difficultyName) {
        this.timeLimitInSeconds = timeLimitInSeconds;
        this.difficultyName = difficultyName != null ? difficultyName.toLowerCase() : "medium";
    }
}
