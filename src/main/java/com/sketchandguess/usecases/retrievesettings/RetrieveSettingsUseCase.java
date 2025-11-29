package com.sketchandguess.usecases.retrievesettings;

import com.sketchandguess.database.UserSettingsDataBase;
import com.sketchandguess.entities.UserSettings;

/**
 * Use case for retrieving the current user's settings.
 * Implements the InputBoundary pattern.
 */
public class RetrieveSettingsUseCase implements RetrieveSettingsInputBoundary {

    private final UserSettingsDataBase userSettingsDataBase;
    private final RetrieveSettingsOutputBoundary presenter;

    /**
     * Constructs a RetrieveSettingsUseCase with the given database and presenter.
     *
     * @param userSettingsDataBase the database used to load settings
     * @param presenter the output boundary to present results
     */
    public RetrieveSettingsUseCase(UserSettingsDataBase userSettingsDataBase, RetrieveSettingsOutputBoundary presenter) {
        this.userSettingsDataBase = userSettingsDataBase;
        this.presenter = presenter;
    }

    @Override
    public void execute(RetrieveSettingsInputData inputData) {
        try {
            // Retrieve settings from database
            UserSettings settings = userSettingsDataBase.getUserSettings();
            
            // Prepare output data
            RetrieveSettingsOutputData outputData = new RetrieveSettingsOutputData(
                settings.getDefaultTimeLimit(),
                settings.getDifficultyName()
            );
            
            // Call presenter
            presenter.present(outputData);
            
        } catch (Exception e) {
            // If retrieval fails, present default values or handle error
            // For now, present default settings
            RetrieveSettingsOutputData defaultData = new RetrieveSettingsOutputData(
                30.0,
                "medium"
            );
            presenter.present(defaultData);
        }
    }
}

