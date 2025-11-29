package com.sketchandguess.usecases.editsettings;

import com.sketchandguess.database.UserSettingsDataBase;
import com.sketchandguess.entities.UserSettings;

/**
 * Use case for editing the user's settings (time limit and difficulty).
 */
public class EditSettingsUseCase {

    private static final int MIN_TIME_LIMIT = 15;
    private static final int MAX_TIME_LIMIT = 45;

    private final UserSettingsDataBase userSettingsDataBase;

    /**
     * Constructs an EditSettingsUseCase with the given database.
     *
     * @param userSettingsDataBase the database used to load and save settings
     */
    public EditSettingsUseCase(UserSettingsDataBase userSettingsDataBase) {
        this.userSettingsDataBase = userSettingsDataBase;
    }

    /**
     * Edits the settings. Validates input and then saves through the database.
     *
     * @param timeLimitInSeconds new time limit (seconds)
     * @param difficultyName     new difficulty ("easy", "medium", "hard")
     * @throws IllegalArgumentException if the input is invalid
     */
    public void editSettings(int timeLimitInSeconds, String difficultyName) {
        // --- Validation ---
        if (timeLimitInSeconds < MIN_TIME_LIMIT || timeLimitInSeconds > MAX_TIME_LIMIT) {
            throw new IllegalArgumentException(
                    "Time limit must be between " + MIN_TIME_LIMIT + " and " + MAX_TIME_LIMIT + " seconds."
            );
        }

        if (difficultyName == null || difficultyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Difficulty cannot be empty.");
        }

        // --- Retrieve and modify entity ---
        UserSettings settings = userSettingsDataBase.getUserSettings();
        settings.setDefaultTimeLimit(timeLimitInSeconds);
        settings.setDifficultyName(difficultyName.toLowerCase());

        // --- Save updated settings ---
        userSettingsDataBase.saveUserSettings(settings);
    }
}

