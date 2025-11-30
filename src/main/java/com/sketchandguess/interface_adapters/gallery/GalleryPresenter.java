package com.sketchandguess.interface_adapters.gallery;

import com.sketchandguess.usecases.gallery.*;

public class GalleryPresenter implements RetrieveGamesOutputBoundary, SearchGamesOutputBoundary {
    private final GalleryViewModel viewModel;

    public GalleryPresenter(GalleryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(RetrieveGamesOutputData outputData) {
        GalleryState state = viewModel.getState();
        state.setGameRecords(outputData.gameRecords);
        viewModel.setState(state);
        viewModel.firePropertyChange("state");

    }

    @Override
    public void present(SearchGamesOutputData outputData) {
        GalleryState state = viewModel.getState();
        state.setGameRecords(outputData.searchedGames);
        state.setSearchQuery(outputData.query);
        viewModel.setState(state);
        viewModel.firePropertyChange("state");
    }
}
