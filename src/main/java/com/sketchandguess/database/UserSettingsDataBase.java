package com.sketchandguess.database;

import com.sketchandguess.entities.UserSettings;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Uses JSON file to store settings between app runs.
 */
public class UserSettingsDataBase {

    // ---- Singleton instance ----
    private static final UserSettingsDataBase INSTANCE = new UserSettingsDataBase();

    public static UserSettingsDataBase getInstance() {
        return INSTANCE;
    }

    // ---- Persistence ----
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_TIME_LIMIT = "defaultTimeLimit";
    private static final String KEY_EXPORT = "defaultExportFormat";
    private static final String KEY_DIFFICULTY = "difficultyName";
    private final String jsonPath = "src/main/resources/user_settings.json";

    private UserSettings currentSettings;

    // private constructor (singleton)
    private UserSettingsDataBase() {
        loadFromJson();
    }

    /**
     * Load stored settings from JSON file (or defaults if file doesn't exist or parsing fails).
     */
    private void loadFromJson() {
        try {
            Path path = Paths.get(jsonPath);
            if (Files.exists(path)) {
                String jsonContent = Files.readString(path);
                JSONObject json = new JSONObject(jsonContent);
                
                String userId = json.optString(KEY_USER_ID, "default-user");
                double timeLimit = json.optDouble(KEY_TIME_LIMIT, 60.0);
                String exportFormat = json.optString(KEY_EXPORT, "png");
                String difficulty = json.optString(KEY_DIFFICULTY, "medium");
                
                currentSettings = new UserSettings(userId, timeLimit, exportFormat);
                currentSettings.setDifficultyName(difficulty);
            } else {
                // File doesn't exist, use defaults
                currentSettings = new UserSettings("default-user", 60.0, "png");
                currentSettings.setDifficultyName("medium");
            }
        } catch (IOException e) {
            // File read error, use defaults
            System.err.println("Error reading user settings JSON file: " + e.getMessage());
            currentSettings = new UserSettings("default-user", 60.0, "png");
            currentSettings.setDifficultyName("medium");
        } catch (Exception e) {
            // JSON parsing error, use defaults
            System.err.println("Error parsing user settings JSON: " + e.getMessage());
            currentSettings = new UserSettings("default-user", 60.0, "png");
            currentSettings.setDifficultyName("medium");
        }
    }

    /**
     * Save currentSettings to JSON file.
     */
    private void saveToJson() {
        try {
            JSONObject json = new JSONObject();
            json.put(KEY_USER_ID, currentSettings.getUserId());
            json.put(KEY_TIME_LIMIT, currentSettings.getDefaultTimeLimit());
            json.put(KEY_EXPORT, currentSettings.getDefaultExportFormat());
            json.put(KEY_DIFFICULTY, currentSettings.getDifficultyName());
            
            Path path = Paths.get(jsonPath);
            // Ensure parent directory exists
            Path parentDir = path.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            // Write JSON with indentation for readability
            Files.writeString(path, json.toString(2));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user settings to JSON file", e);
        }
    }

    public UserSettings getUserSettings() {
        return currentSettings;
    }

    public void saveUserSettings(UserSettings settings) {
        if (settings == null) {
            throw new IllegalArgumentException("UserSettings cannot be null.");
        }
        currentSettings = settings;
        saveToJson(); // <-- persist
    }
}
