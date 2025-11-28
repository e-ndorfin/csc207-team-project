package com.sketchandguess.interface_adapters.gallery_window;

import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.select_game.SelectGameRecordOutputBoundary;

import java.time.format.DateTimeFormatter;

public class GalleryWindowPresenter implements SelectGameRecordOutputBoundary {

    private final GalleryWindowViewModel viewModel;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");  // formats date in the yyy/MM/dd format

    public GalleryWindowPresenter(GalleryWindowViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GameRecord record) {
        GalleryWindowState state = viewModel.getState();
        state.setCurrentRecord(record);
        state.setDateText(record.getDate() != null ? DATE_FORMATTER.format(record.getDate()) : "");  // date if not null, empty string if null
        state.setPromptText(record.getPrompt() != null ? record.getPrompt() : "");  // prompt if not null, empty string if null
        state.setOutcomeText(record.getHasWon() ? "win" : "loss");  // win if True, loss if False
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        GalleryWindowState state = viewModel.getState();
        // If we have an error, set all the state to empty strings so that we don't display any information
        state.setErrorMessage(error);  
        state.setCurrentRecord(null);
        state.setDateText("");
        state.setPromptText("");
        state.setOutcomeText("");
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }
}
