package com.sketchandguess.interface_adapters.game;

import com.sketchandguess.interface_adapters.ViewManagerModel;
import com.sketchandguess.interface_adapters.ViewModel;
import com.sketchandguess.usecases.RecordGameUseCase.RecordGameOutputBoundary;
import com.sketchandguess.usecases.RecordGameUseCase.RecordGameOutputData;

// TODO: Import GameResultViewModel when created, or use GameViewModel if shared.
// For now, assuming we switch to "GameResult" view.

public class GamePresenter implements RecordGameOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    // private final GameResultViewModel gameResultViewModel; // TODO: Add this

    public GamePresenter(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void present(RecordGameOutputData outputData) {
        // 1. Update the ViewModel state (for Game Result view)
        // GameResultState state = gameResultViewModel.getState();
        // state.setHasWon(outputData.hasWon);
        // ... update other fields ...
        // gameResultViewModel.setState(state);
        // gameResultViewModel.firePropertyChange();

        // 2. Switch View
        viewManagerModel.setState("GameResult");
        viewManagerModel.firePropertyChange("view");
    }
}
