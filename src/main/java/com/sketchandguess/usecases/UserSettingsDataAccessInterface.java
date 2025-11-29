package com.sketchandguess.usecases;

import com.sketchandguess.entities.UserSettings;

public interface UserSettingsDataAccessInterface {
    UserSettings getUserSettings();
    void saveUserSettings(UserSettings settings);
}

