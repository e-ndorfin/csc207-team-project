package com.sketchandguess.interface_adapters.gallery_window;

import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.DeleteGameOutputBoundary;
import com.sketchandguess.usecases.select_game.SelectGameRecordOutputBoundary;

public class GalleryWindowPresenter implements SelectGameRecordOutputBoundary, DeleteGameOutputBoundary {

    private final GalleryWindowViewModel viewModel;

    public GalleryWindowPresenter(GalleryWindowViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GameRecord record) {
        GalleryWindowState state = viewModel.getState();
        state.setCurrentRecord(record);
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        GalleryWindowState state = viewModel.getState();
        state.setErrorMessage(error);
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareDeleteSuccessView() {
        GalleryWindowState state = viewModel.getState();
        state.setCurrentRecord(null);
        state.setErrorMessage(null);
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }
}
