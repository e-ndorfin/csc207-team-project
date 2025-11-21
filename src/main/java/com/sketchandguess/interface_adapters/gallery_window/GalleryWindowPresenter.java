package com.sketchandguess.interface_adapters.gallery_window;

import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.select_game.SelectGameRecordOutputBoundary;

public class GalleryWindowPresenter implements SelectGameRecordOutputBoundary {

    private final GalleryWindowViewModel viewModel;

    public GalleryWindowPresenter(GalleryWindowViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GameRecord record) {
        GalleryWindowState state = viewModel.getState();
        state.setCurrentRecord(record);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        GalleryWindowState state = viewModel.getState();
        state.setErrorMessage(error);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
