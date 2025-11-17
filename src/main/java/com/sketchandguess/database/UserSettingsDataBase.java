package com.sketchandguess.database;

import com.sketchandguess.entities.UserSettings;

/**
 * TEMP implementation – just keeps settings in memory.
 * Your teammate can later change this to read/write a file.
 */
public class UserSettingsDataBase {

    // For now we just keep one global settings object in memory
    private static UserSettings currentSettings =
            new UserSettings("default-user", 60, "png");  // default 60 seconds

    public UserSettings getUserSettings() {
        return currentSettings;
    }

    public void saveUserSettings(UserSettings settings) {
        currentSettings = settings;
    }
}
