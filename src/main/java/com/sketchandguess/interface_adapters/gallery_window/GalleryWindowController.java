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
        state.setCurrentRecord(record);
        presenter.presentRecord(record);
    }

    public void deleteGame() {
        GameRecord record = state.getCurrentRecord();
        if (record == null) {
            presenter.presentError("Failed to delete record.");
            return;
        }
        boolean success = deleteGameUseCase.delete(record);
        if (!success) {
            presenter.presentError("Failed to delete record.");
        } else {
            state.setCurrentRecord(null);
            presenter.presentRecord(null);
        }
    }

    public void saveImage() {
        GameRecord record = state.getCurrentRecord();
        if (record == null) {
            presenter.presentError("No image to save.");
            return;
        }
        try {
            boolean success = saveImageUseCase.save(record.getImagePath());
            if (!success) {
                presenter.presentError("Failed to save image.");
            }
        } catch (IOException e) {
            presenter.presentError("Error while saving image.");
        }
    }

    public void selectGameRecord(GameRecord record) {
        selectGameRecordUseCase.execute(record);
    }

    public void goBackToMainMenu() {
        viewManagerModel.setState("MainMenu");
        viewManagerModel.firePropertyChange("view");
    }
}
