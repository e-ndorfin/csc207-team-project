package com.sketchandguess.interface_adapters.gallery_window;

import com.sketchandguess.entities.GameRecord;

public class GalleryWindowPresenter {
    private final GalleryWindowViewModel viewModel;

    public GalleryWindowPresenter(GalleryWindowViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void presentRecord(GameRecord record) {
        if (record == null) {
            viewModel.setImagePath(null);
            viewModel.setPrompt("No record selected");
            viewModel.setErrorMessage("");
            return;
        }
        viewModel.setImagePath(record.getImagePath());
        viewModel.setPrompt(record.getPrompt());
        viewModel.setErrorMessage("");
    }

    public void presentError(String message) {
        viewModel.setErrorMessage(message);
    }
}