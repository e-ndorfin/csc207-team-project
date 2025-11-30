package com.sketchandguess.usecases.editsettings;

import com.sketchandguess.database.UserSettingsDataBase;
import com.sketchandguess.entities.UserSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditSettingsUseCaseTest {

    @Test
    void successTest() {
        // Arrange
        UserSettingsDataBase dataBase = UserSettingsDataBase.getInstance();
        
        // Set up test settings
        UserSettings testSettings = new UserSettings("testUser", 30.0, "png");
        testSettings.setDifficultyName("medium");
        dataBase.saveUserSettings(testSettings);
        
        EditSettingsUseCase useCase = new EditSettingsUseCase(dataBase);
        
        int validTimeLimit = 30;
        String validDifficulty = "Medium"; // Using capital M to test case conversion
        
        // Act
        useCase.editSettings(validTimeLimit, validDifficulty);
        
        // Assert - verify settings were saved correctly
        UserSettings savedSettings = dataBase.getUserSettings();
        assertEquals(validTimeLimit, savedSettings.getDefaultTimeLimit(), 0.001,
                    "Time limit should be updated");
        assertEquals(validDifficulty.toLowerCase(), savedSettings.getDifficultyName(), 
                    "Difficulty should be updated and converted to lowercase");
    }

    @Test
    void failureTimeLimitTooLowTest() {
        // Arrange
        UserSettingsDataBase dataBase = UserSettingsDataBase.getInstance();
        EditSettingsUseCase useCase = new EditSettingsUseCase(dataBase);
        
        int tooLowTimeLimit = 10; // Below minimum of 15
        String validDifficulty = "medium";
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> useCase.editSettings(tooLowTimeLimit, validDifficulty)
        );
        
        assertTrue(exception.getMessage().contains("15"), 
                  "Error message should mention minimum time limit of 15");
        // Verify original settings were not changed
        assertNotEquals(tooLowTimeLimit, dataBase.getUserSettings().getDefaultTimeLimit(), 0.001);
    }

    @Test
    void failureTimeLimitTooHighTest() {
        // Arrange
        UserSettingsDataBase dataBase = UserSettingsDataBase.getInstance();
        EditSettingsUseCase useCase = new EditSettingsUseCase(dataBase);
        
        int tooHighTimeLimit = 50; // Above maximum of 45
        String validDifficulty = "medium";
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> useCase.editSettings(tooHighTimeLimit, validDifficulty)
        );
        
        assertTrue(exception.getMessage().contains("45"), 
                  "Error message should mention maximum time limit of 45");
        // Verify original settings were not changed
        assertNotEquals(tooHighTimeLimit, dataBase.getUserSettings().getDefaultTimeLimit(), 0.001);
    }

    @Test
    void failureNullDifficultyTest() {
        // Arrange
        UserSettingsDataBase dataBase = UserSettingsDataBase.getInstance();
        EditSettingsUseCase useCase = new EditSettingsUseCase(dataBase);
        
        int validTimeLimit = 30;
        String nullDifficulty = null;
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> useCase.editSettings(validTimeLimit, nullDifficulty)
        );
        
        assertEquals("Difficulty cannot be empty.", exception.getMessage());
        // Verify original settings were not changed
        assertNotNull(dataBase.getUserSettings().getDifficultyName());
    }

    @Test
    void failureEmptyDifficultyTest() {
        // Arrange
        UserSettingsDataBase dataBase = UserSettingsDataBase.getInstance();
        EditSettingsUseCase useCase = new EditSettingsUseCase(dataBase);
        
        int validTimeLimit = 30;
        String emptyDifficulty = "";
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> useCase.editSettings(validTimeLimit, emptyDifficulty)
        );
        
        assertEquals("Difficulty cannot be empty.", exception.getMessage());
        // Verify original settings were not changed
        assertNotEquals(emptyDifficulty, dataBase.getUserSettings().getDifficultyName());
    }

    @Test
    void failureWhitespaceDifficultyTest() {
        // Arrange
        UserSettingsDataBase dataBase = UserSettingsDataBase.getInstance();
        EditSettingsUseCase useCase = new EditSettingsUseCase(dataBase);
        
        int validTimeLimit = 30;
        String whitespaceDifficulty = "   ";
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> useCase.editSettings(validTimeLimit, whitespaceDifficulty)
        );
        
        assertEquals("Difficulty cannot be empty.", exception.getMessage());
        // Verify original settings were not changed
        assertNotEquals(whitespaceDifficulty.trim(), dataBase.getUserSettings().getDifficultyName());
    }
}
