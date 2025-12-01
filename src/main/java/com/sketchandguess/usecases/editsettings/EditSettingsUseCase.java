package com.sketchandguess.usecases.editsettings;

import com.sketchandguess.database.UserSettingsDataBase;
import com.sketchandguess.entities.UserSettings;

/**
 * Use case for editing the user's settings (time limit and difficulty).
 * Implements the InputBoundary pattern.
 */
public class EditSettingsUseCase implements EditSettingsInputBoundary {

    private static final int MIN_TIME_LIMIT = 15;
    private static final int MAX_TIME_LIMIT = 45;

    private final UserSettingsDataBase userSettingsDataBase;
    private final EditSettingsOutputBoundary presenter;

    /**
     * Constructs an EditSettingsUseCase with the given database and presenter.
     *
     * @param userSettingsDataBase the database used to load and save settings
     * @param presenter the output boundary to present results
     */
    public EditSettingsUseCase(UserSettingsDataBase userSettingsDataBase, EditSettingsOutputBoundary presenter) {
        this.userSettingsDataBase = userSettingsDataBase;
        this.presenter = presenter;
    }

    @Override
    public void execute(EditSettingsInputData inputData) {
        // --- Validation ---
        if (inputData.timeLimitInSeconds < MIN_TIME_LIMIT || inputData.timeLimitInSeconds > MAX_TIME_LIMIT) {
            EditSettingsOutputData errorData = new EditSettingsOutputData(
                false,
                "Time limit must be between " + MIN_TIME_LIMIT + " and " + MAX_TIME_LIMIT + " seconds.",
                0.0,
                ""
            );
            presenter.present(errorData);
            return;
        }

        if (inputData.difficultyName == null || inputData.difficultyName.trim().isEmpty()) {
            EditSettingsOutputData errorData = new EditSettingsOutputData(
                false,
                "Difficulty cannot be empty.",
                0.0,
                ""
            );
            presenter.present(errorData);
            return;
        }

        try {
            // --- Retrieve and modify entity ---
            UserSettings settings = userSettingsDataBase.getUserSettings();
            settings.setDefaultTimeLimit(inputData.timeLimitInSeconds);
            settings.setDifficultyName(inputData.difficultyName);

            // --- Save updated settings ---
            userSettingsDataBase.saveUserSettings(settings);

            // --- Success response ---
            EditSettingsOutputData successData = new EditSettingsOutputData(
                true,
                null,
                settings.getDefaultTimeLimit(),
                settings.getDifficultyName()
            );
            presenter.present(successData);

        } catch (Exception e) {
            // Handle any unexpected errors
            EditSettingsOutputData errorData = new EditSettingsOutputData(
                false,
                "Failed to save settings: " + e.getMessage(),
                0.0,
                inputData.difficultyName
            );
            presenter.present(errorData);
        }
    }
}

