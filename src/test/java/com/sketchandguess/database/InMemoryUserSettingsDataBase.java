package com.sketchandguess.database;

import com.sketchandguess.entities.UserSettings;

/**
 * In-memory implementation for testing purposes.
 * Provides the same interface as UserSettingsDataBase but without file I/O.
 * Note: This cannot extend UserSettingsDataBase due to private constructor,
 * so it's used directly in tests where UserSettingsDataBase is expected.
 */
public class InMemoryUserSettingsDataBase {
    
    private UserSettings currentSettings;
    
    public InMemoryUserSettingsDataBase() {
        // Default settings for testing
        this.currentSettings = new UserSettings("testUser", 30.0, "png");
        this.currentSettings.setDifficultyName("medium");
    }
    
    public InMemoryUserSettingsDataBase(UserSettings initialSettings) {
        if (initialSettings != null) {
            this.currentSettings = new UserSettings(initialSettings.getUserId(), 
                                                  initialSettings.getDefaultTimeLimit(), 
                                                  initialSettings.getDefaultExportFormat());
            this.currentSettings.setDifficultyName(initialSettings.getDifficultyName());
        } else {
            this.currentSettings = new UserSettings("testUser", 30.0, "png");
            this.currentSettings.setDifficultyName("medium");
        }
    }
    
    public UserSettings getUserSettings() {
        // Return a copy to prevent external modification
        UserSettings copy = new UserSettings(currentSettings.getUserId(),
                                           currentSettings.getDefaultTimeLimit(),
                                           currentSettings.getDefaultExportFormat());
        copy.setDifficultyName(currentSettings.getDifficultyName());
        return copy;
    }
    
    public void saveUserSettings(UserSettings settings) {
        if (settings != null) {
            // Save a copy (without file I/O)
            this.currentSettings = new UserSettings(settings.getUserId(),
                                                  settings.getDefaultTimeLimit(),
                                                  settings.getDefaultExportFormat());
            this.currentSettings.setDifficultyName(settings.getDifficultyName());
        }
    }
    
    // Helper method for testing - get direct access to current settings
    public UserSettings getCurrentSettings() {
        return currentSettings;
    }
    
    // Helper method for testing - set initial settings
    public void setCurrentSettings(UserSettings settings) {
        if (settings != null) {
            this.currentSettings = new UserSettings(settings.getUserId(),
                                                  settings.getDefaultTimeLimit(),
                                                  settings.getDefaultExportFormat());
            this.currentSettings.setDifficultyName(settings.getDifficultyName());
        } else {
            this.currentSettings = new UserSettings("testUser", 30.0, "png");
            this.currentSettings.setDifficultyName("medium");
        }
    }
}
