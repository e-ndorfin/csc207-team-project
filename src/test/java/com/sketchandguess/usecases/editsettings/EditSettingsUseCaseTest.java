package com.sketchandguess.usecases.editsettings;

import com.sketchandguess.database.UserSettingsDataBase;
import com.sketchandguess.entities.UserSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditSettingsUseCaseTest {

    private UserSettingsDataBase dataBase;
    private TestPresenter presenter;
    private EditSettingsUseCase useCase;

    @BeforeEach
    void setUp() {
        dataBase = UserSettingsDataBase.getInstance();
        // Reset database state
        UserSettings defaultSettings = new UserSettings("testUser", 30.0, "png");
        defaultSettings.setDifficultyName("medium");
        dataBase.saveUserSettings(defaultSettings);

        presenter = new TestPresenter();
        useCase = new EditSettingsUseCase(dataBase, presenter);
    }

    @Test
    void successTest() {
        int validTimeLimit = 30;
        String validDifficulty = "Medium"; 

        EditSettingsInputData inputData = new EditSettingsInputData(validTimeLimit, validDifficulty);
        useCase.execute(inputData);

        assertNotNull(presenter.outputData, "Presenter should have received output data");
        assertTrue(presenter.outputData.success);
        assertEquals(validTimeLimit, presenter.outputData.defaultTimeLimit, 0.001);
        assertEquals("medium", presenter.outputData.difficultyName);
        
        // Verify DB update
        UserSettings savedSettings = dataBase.getUserSettings();
        assertEquals(validTimeLimit, savedSettings.getDefaultTimeLimit(), 0.001);
        assertEquals("medium", savedSettings.getDifficultyName());
    }

    @Test
    void failureTimeLimitTooLowTest() {
        int tooLowTimeLimit = 10; 
        String validDifficulty = "medium";

        EditSettingsInputData inputData = new EditSettingsInputData(tooLowTimeLimit, validDifficulty);
        useCase.execute(inputData);

        assertNotNull(presenter.outputData, "Presenter should have received output data");
        assertFalse(presenter.outputData.success);
        assertTrue(presenter.outputData.errorMessage.contains("15"));
    }

    @Test
    void failureTimeLimitTooHighTest() {
        int tooHighTimeLimit = 50; 
        String validDifficulty = "medium";

        EditSettingsInputData inputData = new EditSettingsInputData(tooHighTimeLimit, validDifficulty);
        useCase.execute(inputData);

        assertNotNull(presenter.outputData, "Presenter should have received output data");
        assertFalse(presenter.outputData.success);
        assertTrue(presenter.outputData.errorMessage.contains("45"));
    }

    @Test
    void failureEmptyDifficultyTest() {
        int validTimeLimit = 30;
        String emptyDifficulty = "";

        EditSettingsInputData inputData = new EditSettingsInputData(validTimeLimit, emptyDifficulty);
        useCase.execute(inputData);

        assertNotNull(presenter.outputData, "Presenter should have received output data");
        assertFalse(presenter.outputData.success);
        assertEquals("Difficulty cannot be empty.", presenter.outputData.errorMessage);
    }

    @Test
    void failureWhitespaceDifficultyTest() {
        int validTimeLimit = 30;
        String whitespaceDifficulty = "   ";

        EditSettingsInputData inputData = new EditSettingsInputData(validTimeLimit, whitespaceDifficulty);
        useCase.execute(inputData);

        assertNotNull(presenter.outputData, "Presenter should have received output data");
        assertFalse(presenter.outputData.success);
        assertEquals("Difficulty cannot be empty.", presenter.outputData.errorMessage);
    }

    private static class TestPresenter implements EditSettingsOutputBoundary {
        EditSettingsOutputData outputData;

        @Override
        public void present(EditSettingsOutputData outputData) {
            this.outputData = outputData;
        }
    }
}