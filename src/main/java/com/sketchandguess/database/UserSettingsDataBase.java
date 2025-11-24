package com.sketchandguess.database;

import com.sketchandguess.entities.UserSettings;

/**
 * Simple in-memory "database" that stores the current user's settings.
 */
public class UserSettingsDataBase {

    private UserSettings currentSettings;

    /**
     * Creates a new UserSettingsDataBase with default settings.
     */
    public UserSettingsDataBase() {
        // default values for a new user
        String defaultUserId = "default-user";
        double defaultTimeLimit = 60;      // 60 seconds default
        String defaultExportFormat = "png";

        this.currentSettings =
                new UserSettings(defaultUserId, defaultTimeLimit, defaultExportFormat);
    }

    /**
     * Optionally allow providing initial settings.
     */
    public UserSettingsDataBase(UserSettings initialSettings) {
        if (initialSettings == null) {
            throw new IllegalArgumentException("initialSettings cannot be null.");
        }
        this.currentSettings = initialSettings;
    }

    /**
     * Returns the current UserSettings object.
     */
    public UserSettings getUserSettings() {
        return currentSettings;
    }

    /**
     * Saves the given UserSettings as the current settings.
     *
     * @param settings the new settings; must not be null
     */
    public void saveUserSettings(UserSettings settings) {
        if (settings == null) {
            throw new IllegalArgumentException("UserSettings cannot be null.");
        }
        this.currentSettings = settings;
    }
}
