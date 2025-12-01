package com.sketchandguess.usecases.deletegame;

import com.sketchandguess.database.InMemoryGameDataAccess;
import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.entities.GameRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DeleteGameUseCaseTest {

    @Test
    void successTest() {
        // Arrange
        InMemoryGameDataAccess dataAccess = new InMemoryGameDataAccess();
        Difficulty difficulty = new Difficulty("Medium");
        
        // Create a test game record
        GameRecord testGame = new GameRecord(
            "test/image.png",
            LocalDate.now(),
            true,
            45.5,
            60.0,
            difficulty,
            "A fast red car",
            "sports car"
        );
        
        // Add the game to the database
        dataAccess.save(testGame);
        
        DeleteGameUseCase useCase = new DeleteGameUseCase(dataAccess);
        
        // Act
        String result = useCase.delete(testGame);
        
        // Assert
        assertNull(result, "Deletion should succeed for existing game");
        assertEquals(0, dataAccess.getGames().size(), "Database should be empty after deletion");
    }

    @Test
    void failureTest() {
        // Arrange
        InMemoryGameDataAccess dataAccess = new InMemoryGameDataAccess();
        Difficulty difficulty = new Difficulty("Medium");
        
        // Create a game record that doesn't exist in the database
        GameRecord nonExistentGame = new GameRecord(
            "nonexistent/image.png",
            LocalDate.now(),
            false,
            30.0,
            60.0,
            difficulty,
            "A blue house",
            "house"
        );
        
        DeleteGameUseCase useCase = new DeleteGameUseCase(dataAccess);
        
        // Act
        String result = useCase.delete(nonExistentGame);
        
        // Assert
        assertNotNull(result, "Deletion should fail for non-existent game");
        assertEquals(0, dataAccess.getGames().size(), "Database should remain empty");
    }

    @Test
    void failureNullGameTest() {
        // Arrange
        InMemoryGameDataAccess dataAccess = new InMemoryGameDataAccess();
        DeleteGameUseCase useCase = new DeleteGameUseCase(dataAccess);
        
        // Act & Assert
        assertNotNull(useCase.delete(null), "Deletion of null should return false");
        assertEquals(0, dataAccess.getGames().size(), "Database should remain empty");
    }
}
