package com.sketchandguess.usecases;

import com.sketchandguess.database.InMemoryGameDataAccess;
import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.recordgame.RecordGameInputBoundary;
import com.sketchandguess.usecases.recordgame.RecordGameInputData;
import com.sketchandguess.usecases.recordgame.RecordGameOutputBoundary;
import com.sketchandguess.usecases.recordgame.RecordGameOutputData;
import com.sketchandguess.usecases.recordgame.RecordGameUseCase;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RecordGameUseCaseTest {

    @Test
    void successTest() {
        // Arrange
        InMemoryGameDataAccess dataAccess = new InMemoryGameDataAccess();
        Difficulty difficulty = new Difficulty("Medium");
        BufferedImage dummyImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        
        RecordGameInputData inputData = new RecordGameInputData(
                true,
                "A fast red car",
                45.5,
                60.0,
                difficulty,
                dummyImage,
                dummyImage, // Not used in this use case, but required
                "sports car"
        );

        // Create presenter that tests whether the test case is as we expect
        RecordGameOutputBoundary successPresenter = new RecordGameOutputBoundary() {
            @Override
            public void present(RecordGameOutputData outputData) {
                // Assertions inside the presenter method - using static imports
                assertEquals(true, outputData.hasWon, "Should have won");
                assertEquals("A fast red car", outputData.prompt, "Prompt should match");
                assertEquals(45.5, outputData.timeTaken, 0.001, "Time taken should match");
                assertEquals(60.0, outputData.timeLimit, 0.001, "Time limit should match");
                assertEquals("sports car", outputData.aiGuess, "AI guess should match");
                assertNotNull(outputData.date, "Date should not be null");
                
                // Verify data was saved correctly
                assertEquals(1, dataAccess.getGames().size(), "One game should be saved");
                GameRecord savedRecord = dataAccess.getGames().get(0);
                assertNotNull(savedRecord.getImagePath(), "Image path should not be null");
                assertTrue(savedRecord.getImagePath().startsWith("src/main/resources/images/"), "Image path should be in the correct directory");
                assertTrue(savedRecord.getImagePath().endsWith(".png"), "Image path should end with .png");
                assertEquals(outputData.imagePath, savedRecord.getImagePath(), "Output data and saved record should have same image path");
                assertEquals("A fast red car", savedRecord.getPrompt(), "Saved prompt should match");
                assertEquals(45.5, savedRecord.getTimeTaken(), 0.001, "Saved time taken should match");
                assertTrue(Math.abs(LocalDate.now().toEpochDay() - savedRecord.getDate().toEpochDay()) <= 1, 
                          "Date should be today");
            }
        };

        RecordGameInputBoundary useCase = new RecordGameUseCase(dataAccess, successPresenter);
        
        // Act
        useCase.execute(inputData);
    }

    @Test
    void lossTest() {
        // Arrange
        InMemoryGameDataAccess dataAccess = new InMemoryGameDataAccess();
        Difficulty difficulty = new Difficulty("Medium");
        BufferedImage dummyImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

        RecordGameInputData inputData = new RecordGameInputData(
                false,  // Lost the game
                "A fast red car",
                55.0,  // Took longer than time limit
                60.0,
                difficulty,
                dummyImage,
                dummyImage, // Not used in this use case, but required
                "wrong guess"
        );

        // Create presenter that tests the loss scenario
        RecordGameOutputBoundary lossPresenter = new RecordGameOutputBoundary() {
            @Override
            public void present(RecordGameOutputData outputData) {
                // Verify loss was recorded correctly
                assertEquals(false, outputData.hasWon, "Should not have won");
                assertEquals("A fast red car", outputData.prompt, "Prompt should match");
                assertEquals(55.0, outputData.timeTaken, 0.001, "Time taken should match");
                assertEquals("wrong guess", outputData.aiGuess, "AI guess should match");
                
                // Verify data was saved correctly
                assertEquals(1, dataAccess.getGames().size(), "One game should be saved");
                GameRecord savedRecord = dataAccess.getGames().get(0);
                assertEquals(false, savedRecord.getHasWon(), "Saved record should show loss");
                assertEquals(55.0, savedRecord.getTimeTaken(), 0.001, "Saved time taken should match");
                assertNotNull(savedRecord.getImagePath(), "Image path should not be null");
            }
        };

        RecordGameInputBoundary useCase = new RecordGameUseCase(dataAccess, lossPresenter);
        
        // Act
        useCase.execute(inputData);
    }
}
