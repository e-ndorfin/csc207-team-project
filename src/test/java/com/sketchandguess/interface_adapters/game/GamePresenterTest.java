package com.sketchandguess.interface_adapters.game;

import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.interface_adapters.ViewManagerModel;
import com.sketchandguess.usecases.recordgame.RecordGameOutputData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamePresenterTest {

    @Test
    void testPresentUpdatesViewModelAndSwitchesView() {
        // Arrange
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        GameResultViewModel gameResultViewModel = new GameResultViewModel();
        GameViewModel gameViewModel = new GameViewModel();
        GamePresenter presenter = new GamePresenter(viewManagerModel, gameResultViewModel, gameViewModel);

        RecordGameOutputData outputData = new RecordGameOutputData(
            true,                   // hasWon
            "Test Prompt",          // prompt
            30.0,                   // timeTaken
            60.0,                   // timeLimit
            "2023-01-01",           // date
            new Difficulty("Easy"), // difficulty
            "path/to/image.png",    // imagePath
            "Correct Guess"         // aiGuess
        );

        // Act
        presenter.present(outputData);

        // Assert - Check State
        GameResultState state = gameResultViewModel.getState();
        assertEquals("Test Prompt", state.getPrompt());
        assertEquals("Correct Guess", state.getAiGuess());
        assertEquals(30.0, state.getTimeTaken());
        assertEquals(60.0, state.getTimeLimit());
        assertTrue(state.isHasWon());
        assertEquals("path/to/image.png", state.getImagePath());
        
        // Logic in Presenter: if (outputData.hasWon) -> "You Win! AI guessed correctly!"
        assertEquals("You Win! AI guessed correctly!", state.getEndingMessage());

        // Assert - Check View Switch
        assertEquals(GameResultViewModel.VIEW_NAME, viewManagerModel.getState());
    }

    @Test
    void testPresentLossTimeout() {
        // Arrange
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        GameResultViewModel gameResultViewModel = new GameResultViewModel();
        GameViewModel gameViewModel = new GameViewModel();
        GamePresenter presenter = new GamePresenter(viewManagerModel, gameResultViewModel, gameViewModel);

        RecordGameOutputData outputData = new RecordGameOutputData(
            false,
            "Prompt",
            60.0,
            60.0,
            "2023-01-01",
            new Difficulty("Hard"),
            "img.png",
            "Wrong Guess"
        );

        // Act
        presenter.present(outputData);

        // Assert
        GameResultState state = gameResultViewModel.getState();
        assertFalse(state.isHasWon());
        assertEquals("You Lose! Time's up! ", state.getEndingMessage());
    }
}
