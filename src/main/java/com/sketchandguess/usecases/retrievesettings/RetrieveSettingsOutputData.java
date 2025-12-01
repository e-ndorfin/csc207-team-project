package com.sketchandguess.usecases.retrievesettings;

/**
 * Data returned from Use Case → Presenter for RetrieveSettings.
 * Contains the current time limit and difficulty settings.
 */
public class RetrieveSettingsOutputData {
    public final double defaultTimeLimit;
    public final String difficultyName;

    public RetrieveSettingsOutputData(double defaultTimeLimit, String difficultyName) {
        this.defaultTimeLimit = defaultTimeLimit;
        this.difficultyName = difficultyName != null ? difficultyName.toLowerCase() : "medium";
    }
}
