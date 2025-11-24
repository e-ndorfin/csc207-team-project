package com.sketchandguess.interface_adapters.gallery_window;

import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.select_game.SelectGameRecordInputBoundary;

public class GalleryWindowController {

    private final SelectGameRecordInputBoundary selectGameRecordUseCase;

    public GalleryWindowController(SelectGameRecordInputBoundary selectGameRecordUseCase) {
        this.selectGameRecordUseCase = selectGameRecordUseCase;
    }

    public void selectGameRecord(GameRecord record) {
        selectGameRecordUseCase.execute(record);
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
