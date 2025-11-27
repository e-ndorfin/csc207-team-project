package com.sketchandguess.interface_adapters.game;

import com.sketchandguess.interface_adapters.ViewManagerModel;
import com.sketchandguess.usecases.RecordGameUseCase.RecordGameOutputBoundary;
import com.sketchandguess.usecases.RecordGameUseCase.RecordGameOutputData;

// For now, assuming we switch to "GameResult" view.

public class GamePresenter implements RecordGameOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final GameResultViewModel gameResultViewModel ;

    public GamePresenter(ViewManagerModel viewManagerModel,
                         GameResultViewModel gameResultViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.gameResultViewModel = gameResultViewModel;
    }

    @Override
    public void present(RecordGameOutputData outputData) {
        // 1. Update the ViewModel state (for Game Result view)
        GameResultState state = gameResultViewModel.getState();
        // state.setHasWon(outputData.hasWon);
        // ... update other fields ...
        // gameResultViewModel.setState(state);
        // gameResultViewModel.firePropertyChange();
        state.setPrompt(outputData.prompt);
        state.setAiGuess(outputData.aiGuess);
        state.setTimeTaken(outputData.timeTaken);
        state.setTimeLimit(outputData.timeLimit);
        state.setHasWon(outputData.hasWon);
        state.setImagePath(outputData.imagePath);

        boolean timeOut = outputData.timeTaken >= outputData.timeLimit - 1e-6;
        String ending;
        if (outputData.hasWon) {
            ending = "You Win! AI guessed correctly!";
        } else if (timeOut) {
            ending = "You Lose! Time's up! ";
        } else {
            ending = "You Lose! AI guessed wrong!";
        }
        state.setEndingMessage(ending);

        gameResultViewModel.setState(state);

        // 2. Switch View
        viewManagerModel.setState(GameResultViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChange("view");
    }
}
