package com.sketchandguess.interface_adapters.game;

import javax.swing.SwingUtilities;

import com.sketchandguess.interface_adapters.ViewManagerModel;
import com.sketchandguess.usecases.recordgame.RecordGameOutputBoundary;
import com.sketchandguess.usecases.recordgame.RecordGameOutputData;
import com.sketchandguess.usecases.gameplay.GameplayOutputBoundary;
import com.sketchandguess.usecases.gameplay.GameplayOutputData;

public class GamePresenter implements RecordGameOutputBoundary, GameplayOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final GameResultViewModel gameResultViewModel;
    private final GameViewModel gameViewModel;

    public GamePresenter(ViewManagerModel viewManagerModel,
                         GameResultViewModel gameResultViewModel,
                         GameViewModel gameViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.gameResultViewModel = gameResultViewModel;
        this.gameViewModel = gameViewModel;
    }

    @Override
    public void present(RecordGameOutputData outputData) {
        // 1. Update the ViewModel state (for Game Result view)
        GameResultState state = gameResultViewModel.getState();
        state.setPrompt(outputData.prompt);
        state.setAiGuess(outputData.aiGuess);
        state.setTimeTaken(outputData.timeTaken);
        state.setTimeLimit(outputData.timeLimit);
        state.setHasWon(outputData.hasWon);
        state.setImagePath(outputData.imagePath);

        boolean timeOut = outputData.timeTaken >= outputData.timeLimit;
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
        gameResultViewModel.firePropertyChange("state");

        // 2. Switch View
        viewManagerModel.setState(GameResultViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChange("view");
    }

    @Override
    public void present(GameplayOutputData outputData) {
        SwingUtilities.invokeLater(() -> {
            GameState state = gameViewModel.getState();
            if (state == null) {
                state = new GameState();
            }
            state.setPredictions(outputData.getPredictions());
            state.setHasWon(outputData.hasWon());
            
            // System.out.println("[DEBUG] GamePresenter: Setting hasWon = " + outputData.hasWon());
            // System.out.println("[DEBUG] GamePresenter: State hasWon = " + state.isHasWon());
            
            gameViewModel.setState(state);
            gameViewModel.firePropertyChange("state");
            
            // System.out.println("[DEBUG] GamePresenter: Fired property change for state");
        });
    }
}
