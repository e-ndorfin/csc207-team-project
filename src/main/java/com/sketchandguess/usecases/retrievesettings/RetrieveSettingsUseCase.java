package com.sketchandguess.usecases.retrievesettings;

import com.sketchandguess.database.UserSettingsDataBase;
import com.sketchandguess.entities.UserSettings;

/**
 * Use case for retrieving the current user's settings.
 */
public class RetrieveSettingsUseCase {

    private final UserSettingsDataBase userSettingsDataBase;

    /**
     * Constructs a RetrieveSettingsUseCase with the given database.
     *
     * @param userSettingsDataBase the database used to load settings
     */
    public RetrieveSettingsUseCase(UserSettingsDataBase userSettingsDataBase) {
        this.userSettingsDataBase = userSettingsDataBase;
    }

    /**
     * Returns the current UserSettings stored in the database.
     */
    public UserSettings retrieveSettings() {
        return userSettingsDataBase.getUserSettings();
    }
}

