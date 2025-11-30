package com.sketchandguess.interface_adapters.gallery_window;

import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.deletegame.DeleteGameInputBoundary;
import com.sketchandguess.usecases.saveimagetouser.SaveImageToUserInputBoundary;
import com.sketchandguess.usecases.selectgame.SelectGameRecordInputBoundary;
import com.sketchandguess.interface_adapters.ViewManagerModel;
import java.io.IOException;

public class GalleryWindowController {
    private final GalleryWindowState state;
    private final GalleryWindowPresenter presenter;
    private final DeleteGameInputBoundary deleteGameUseCase;
    private final SaveImageToUserInputBoundary saveImageUseCase;
    private final SelectGameRecordInputBoundary selectGameRecordUseCase;
    private final ViewManagerModel viewManagerModel;

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

    public void setRecord(GameRecord record) {
        // Clear the record when window closes - this allows the same record to be reopened
        presenter.presentRecord(record);
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
        String error = deleteGameUseCase.delete(record);
        boolean success = (error == null);
        if (!success) {
            presenter.prepareFailView(error);
        } else {
            state.setCurrentRecord(null);
            presenter.presentDeletionSuccess();
        }
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

    public void selectGameRecord(GameRecord record) {
        selectGameRecordUseCase.execute(record);
    }
}
