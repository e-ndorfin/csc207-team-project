package com.sketchandguess.database;

import com.sketchandguess.entities.UserSettings;

import java.util.prefs.Preferences;

/**
 * Singleton + persistent UserSettings database.
 * Uses Java Preferences to store settings between app runs.
 */
public class UserSettingsDataBase {

    // ---- Singleton instance ----
    private static final UserSettingsDataBase INSTANCE = new UserSettingsDataBase();

    public static UserSettingsDataBase getInstance() {
        return INSTANCE;
    }

    // ---- Persistence ----
    private static final String PREF_NODE = "com.sketchandguess.settings";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_TIME_LIMIT = "defaultTimeLimit";
    private static final String KEY_EXPORT = "defaultExportFormat";
    private static final String KEY_DIFFICULTY = "difficultyName";

    private final Preferences prefs;
    private UserSettings currentSettings;

    // private constructor (singleton)
    private UserSettingsDataBase() {
        prefs = Preferences.userRoot().node(PREF_NODE);
        loadFromPrefs();
    }

    /**
     * Load stored settings (or defaults if none stored yet).
     */
    private void loadFromPrefs() {
        String userId = prefs.get(KEY_USER_ID, "default-user");
        double timeLimit = prefs.getDouble(KEY_TIME_LIMIT, 60);
        String exportFormat = prefs.get(KEY_EXPORT, "png");
        String difficulty = prefs.get(KEY_DIFFICULTY, "medium");

        currentSettings = new UserSettings(userId, timeLimit, exportFormat);
        currentSettings.setDifficultyName(difficulty);
    }

    /**
     * Save currentSettings into Preferences.
     */
    private void saveToPrefs() {
        prefs.put(KEY_USER_ID, currentSettings.getUserId());
        prefs.putDouble(KEY_TIME_LIMIT, currentSettings.getDefaultTimeLimit());
        prefs.put(KEY_EXPORT, currentSettings.getDefaultExportFormat());
        prefs.put(KEY_DIFFICULTY, currentSettings.getDifficultyName());
    }

    public UserSettings getUserSettings() {
        return currentSettings;
    }

    public void saveUserSettings(UserSettings settings) {
        if (settings == null) {
            throw new IllegalArgumentException("UserSettings cannot be null.");
        }
        currentSettings = settings;
        saveToPrefs(); // <-- persist
    }
}
