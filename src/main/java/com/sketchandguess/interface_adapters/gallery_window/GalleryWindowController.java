package com.sketchandguess.interface_adapters.gallery_window;

import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.interface_adapters.ViewManagerModel;
import com.sketchandguess.usecases.select_game.SelectGameRecordInputBoundary;

import com.sketchandguess.usecases.DeleteGameInputBoundary;
import com.sketchandguess.usecases.SaveImageToUserInputBoundary;
import java.io.IOException;

public class GalleryWindowController extends GalleryWindowPresenter {
    private final SelectGameRecordInputBoundary selectGameRecordUseCase;
    private final ViewManagerModel viewManagerModel;
    private final GalleryWindowState state;
    private final GalleryWindowPresenter presenter;
    private final DeleteGameInputBoundary deleteGameUseCase;
    private final SaveImageToUserInputBoundary saveImageUseCase;

    public GalleryWindowController(GalleryWindowState state,
                                   GalleryWindowPresenter presenter,
                                   DeleteGameInputBoundary deleteGameUseCase,
                                   SaveImageToUserInputBoundary saveImageUseCase,
                                   SelectGameRecordInputBoundary selectGameRecordUseCase,
                                   ViewManagerModel viewManagerModel) {
        this.state = state;
        this.presenter = presenter;
        this.deleteGameUseCase = deleteGameUseCase;
        this.saveImageUseCase = saveImageUseCase;
        this.selectGameRecordUseCase = selectGameRecordUseCase;
        this.viewManagerModel = viewManagerModel;
    }

    public void selectGameRecord(GameRecord record) {
        selectGameRecordUseCase.execute(record);
    }

    public void goBackToMainMenu() {
        viewManagerModel.setState("MainMenu");
        viewManagerModel.firePropertyChange("view");
    }

    public void deleteGame() {
        GameRecord record = state.getCurrentRecord();
        if (record == null) {
            presenter.prepareFailView("No record selected to delete.");
            return;
        }
        deleteGameUseCase.delete(record);
    }

    public void saveImage() {
        GameRecord record = state.getCurrentRecord();
        if (record == null) {
            presenter.prepareFailView("No image selected to save.");
            return;
        }
        try {
            boolean success = saveImageUseCase.save(record.getImagePath());
            if (!success) {
                presenter.prepareFailView("Failed to save image to local disk.");
            }
        } catch (IOException e) {
            presenter.prepareFailView("Error while saving: " + e.getMessage());
        }
    }
}
