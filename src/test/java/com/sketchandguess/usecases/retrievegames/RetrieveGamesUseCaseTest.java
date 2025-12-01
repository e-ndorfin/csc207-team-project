package com.sketchandguess.usecases.retrievegames;

import com.sketchandguess.database.InMemoryGameDataAccess;
import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.gallery.RetrieveGamesInputData;
import com.sketchandguess.usecases.gallery.RetrieveGamesOutputBoundary;
import com.sketchandguess.usecases.gallery.RetrieveGamesOutputData;
import com.sketchandguess.usecases.gallery.RetrieveGamesUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RetrieveGamesUseCaseTest {

    private InMemoryGameDataAccess dataAccess;
    private RetrieveGamesUseCase useCase;
    private Difficulty mediumDifficulty;
    private TestPresenter presenter;

    private static class TestPresenter implements RetrieveGamesOutputBoundary {
        RetrieveGamesOutputData receivedData;

        @Override
        public void present(RetrieveGamesOutputData data) {
            this.receivedData = data;
        }
    }

    @BeforeEach
    void setUp() {
        dataAccess = new InMemoryGameDataAccess();
        presenter = new TestPresenter();
        useCase = new RetrieveGamesUseCase(dataAccess, presenter);
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
        useCase.execute(new RetrieveGamesInputData());
        List<GameRecord> result = presenter.receivedData.gameRecords;
        
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
    void emptyDatabaseGetGamesTest() {
        // Arrange - create new empty database
        InMemoryGameDataAccess emptyDataAccess = new InMemoryGameDataAccess();
        TestPresenter emptyPresenter = new TestPresenter();
        RetrieveGamesUseCase emptyUseCase = new RetrieveGamesUseCase(emptyDataAccess, emptyPresenter);
        
        // Act
        emptyUseCase.execute(new RetrieveGamesInputData());
        List<GameRecord> result = emptyPresenter.receivedData.gameRecords;
        
        // Assert
        assertNotNull(result, "Should return a non-null list");
        assertEquals(0, result.size(), "Should return empty list for empty database");
    }
}