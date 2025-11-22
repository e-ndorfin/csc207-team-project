package com.sketchandguess.interface_adapters.gallery_window;

import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.interface_adapters.ViewManagerModel;
import com.sketchandguess.usecases.select_game.SelectGameRecordInputBoundary;

public class GalleryWindowController {

    private final SelectGameRecordInputBoundary selectGameRecordUseCase;
    private final ViewManagerModel viewManagerModel;

    public GalleryWindowController(SelectGameRecordInputBoundary selectGameRecordUseCase,
                                   ViewManagerModel viewManagerModel) {
        this.selectGameRecordUseCase = selectGameRecordUseCase;
        this.viewManagerModel = viewManagerModel;
    }

    public void selectGameRecord(GameRecord record) {
        selectGameRecordUseCase.execute(record);
    }

    public void goBackToMainMenu() {
        viewManagerModel.setState("MainMenu"); // Assuming "MainMenu" is the view name for the main menu
        viewManagerModel.firePropertyChange("view");
    }

    // Placeholder for saveImage and deleteGame, which will be implemented later
    // These would likely interact with other use cases (SaveImageToUserUseCase, DeleteGameUseCase)
    public void saveImage() {
        // TODO: Implement save image logic
        System.out.println("Save image called in controller.");
    }

    public void deleteGame() {
        // TODO: Implement delete game logic
        System.out.println("Delete game called in controller.");
    }
}
