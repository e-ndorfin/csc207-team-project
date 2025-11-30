package com.sketchandguess.usecases.retrievesettings;

import com.sketchandguess.database.InMemoryUserSettingsDataBase;
import com.sketchandguess.entities.UserSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RetrieveSettingsUseCaseTest {

    @Test
    void successTest() {
        // Arrange
        
        // Create in-memory settings
        InMemoryUserSettingsDataBase inMemoryDataBase = new InMemoryUserSettingsDataBase();
        UserSettings testSettings = new UserSettings("testUser", 30.0, "png");
        testSettings.setDifficultyName("hard");
        inMemoryDataBase.setCurrentSettings(testSettings);
        
        UserSettings retrievedSettings = inMemoryDataBase.getUserSettings();
        
        // Assert
        assertNotNull(retrievedSettings, "Should return non-null settings");
        assertEquals("testUser", retrievedSettings.getUserId(), "User ID should match");
        assertEquals(30.0, retrievedSettings.getDefaultTimeLimit(), 0.001, "Time limit should match");
        assertEquals("hard", retrievedSettings.getDifficultyName(), "Difficulty should match");
        assertEquals("png", retrievedSettings.getDefaultExportFormat(), "Export format should match");
    }
}

