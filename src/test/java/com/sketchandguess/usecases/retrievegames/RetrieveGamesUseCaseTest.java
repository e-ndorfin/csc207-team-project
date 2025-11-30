package com.sketchandguess.usecases.retrievegames;

import com.sketchandguess.database.InMemoryGameDataAccess;
import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.entities.GameRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RetrieveGamesUseCaseTest {

    private InMemoryGameDataAccess dataAccess;
    private RetrieveGamesUseCase useCase;
    private Difficulty mediumDifficulty;

    @BeforeEach
    void setUp() {
        dataAccess = new InMemoryGameDataAccess();
        useCase = new RetrieveGamesUseCase(dataAccess);
        mediumDifficulty = new Difficulty("Medium");
        
        // Add test data
        GameRecord game1 = new GameRecord(
            "image1.png",
            LocalDate.now(),
            true,
            45.5,
            60.0,
            mediumDifficulty,
            "A fast red car",
            "sports car"
        );
        
        GameRecord game2 = new GameRecord(
            "image2.png",
            LocalDate.now(),
            false,
            30.0,
            60.0,
            mediumDifficulty,
            "A blue house",
            "house"
        );
        
        GameRecord game3 = new GameRecord(
            "image3.png",
            LocalDate.now(),
            true,
            40.0,
            60.0,
            mediumDifficulty,
            "A green tree",
            "tree"
        );
        
        dataAccess.save(game1);
        dataAccess.save(game2);
        dataAccess.save(game3);
    }

    @Test
    void successGetGamesTest() {
        // Act
        List<GameRecord> result = useCase.GetGames();
        
        // Assert
        assertNotNull(result, "Should return a non-null list");
        assertEquals(3, result.size(), "Should return all 3 games");
        assertTrue(result.stream().anyMatch(game -> game.getPrompt().equals("A fast red car")), 
                  "Should include first game");
        assertTrue(result.stream().anyMatch(game -> game.getPrompt().equals("A blue house")), 
                  "Should include second game");
        assertTrue(result.stream().anyMatch(game -> game.getPrompt().equals("A green tree")), 
                  "Should include third game");
    }

    @Test
    void successSearchGamesTest() {
        // Act - search for games containing "car"
        List<GameRecord> result = useCase.SearchGames("car");
        
        // Assert
        assertNotNull(result, "Should return a non-null list");
        assertEquals(1, result.size(), "Should return 1 matching game");
        assertEquals("A fast red car", result.get(0).getPrompt(), "Should return the car game");
    }

    @Test
    void successSearchGamesNoMatchesTest() {
        // Act - search for something that doesn't exist
        List<GameRecord> result = useCase.SearchGames("elephant");
        
        // Assert
        assertNotNull(result, "Should return a non-null list");
        assertEquals(0, result.size(), "Should return empty list for no matches");
    }

    @Test
    void successSearchGamesEmptyQueryTest() {
        // Act - search with empty string (should return all games)
        List<GameRecord> result = useCase.SearchGames("");
        
        // Assert
        assertNotNull(result, "Should return a non-null list");
        assertEquals(3, result.size(), "Should return all games for empty query");
    }

    @Test
    void successGetGameTest() {
        // Act - get first game (index 0)
        GameRecord result = useCase.GetGame(0);
        
        // Assert
        assertNotNull(result, "Should return a valid game");
        assertEquals("A fast red car", result.getPrompt(), "Should return first game");
    }

    @Test
    void failureGetGameInvalidCodeTest() {
        // Act - get game with invalid index
        GameRecord result = useCase.GetGame(10); // Beyond array bounds
        
        // Assert
        assertNull(result, "Should return null for invalid game code");
    }

    @Test
    void failureGetGameNegativeCodeTest() {
        // Act - get game with negative index
        GameRecord result = useCase.GetGame(-1);
        
        // Assert
        assertNull(result, "Should return null for negative game code");
    }

    @Test
    void emptyDatabaseGetGamesTest() {
        // Arrange - create new empty database
        InMemoryGameDataAccess emptyDataAccess = new InMemoryGameDataAccess();
        RetrieveGamesUseCase emptyUseCase = new RetrieveGamesUseCase(emptyDataAccess);
        
        // Act
        List<GameRecord> result = emptyUseCase.GetGames();
        
        // Assert
        assertNotNull(result, "Should return a non-null list");
        assertEquals(0, result.size(), "Should return empty list for empty database");
    }
}
