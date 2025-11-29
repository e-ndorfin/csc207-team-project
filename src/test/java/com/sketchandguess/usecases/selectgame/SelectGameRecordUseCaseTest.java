package com.sketchandguess.usecases.selectgame;

import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.entities.GameRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SelectGameRecordUseCaseTest {

    @Test
    void successTest() {
        // Arrange
        Difficulty difficulty = new Difficulty("Medium");
        GameRecord testRecord = new GameRecord(
            "test/image.png",
            LocalDate.now(),
            true,
            45.5,
            60.0,
            difficulty,
            "A fast red car",
            "sports car"
        );

        // Create presenter that tests whether the test case is as we expect
        SelectGameRecordOutputBoundary successPresenter = new SelectGameRecordOutputBoundary() {
            @Override
            public void prepareSuccessView(GameRecord record) {
                // Assertions inside the presenter method
                assertNotNull(record, "Record should not be null");
                assertEquals("test/image.png", record.getImagePath(), "Image path should match");
                assertEquals("A fast red car", record.getPrompt(), "Prompt should match");
                assertEquals(true, record.getHasWon(), "Has won should match");
                assertEquals(45.5, record.getTimeTaken(), 0.001, "Time taken should match");
            }

            @Override
            public void prepareFailView(String error) {
                // This should never be reached since the test case should succeed
                fail("Use case failure is unexpected.");
            }
        };

        SelectGameRecordUseCase useCase = new SelectGameRecordUseCase(successPresenter);
        
        // Act
        useCase.execute(testRecord);
    }

    @Test
    void failureNullRecordTest() {
        // Arrange
        // Create presenter that tests whether the test case is as we expect
        SelectGameRecordOutputBoundary failurePresenter = new SelectGameRecordOutputBoundary() {
            @Override
            public void prepareSuccessView(GameRecord record) {
                // This should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                // Assertions inside the presenter method
                assertEquals("Selected record is invalid.", error, "Error message should match");
            }
        };

        SelectGameRecordUseCase useCase = new SelectGameRecordUseCase(failurePresenter);
        
        // Act
        useCase.execute(null);
    }
}

