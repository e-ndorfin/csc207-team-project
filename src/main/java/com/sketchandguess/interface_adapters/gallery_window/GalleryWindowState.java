package com.sketchandguess.interface_adapters.gallery_window;

import com.sketchandguess.entities.GameRecord;

public class GalleryWindowState {
    private GameRecord currentRecord = null;
    private String errorMessage = "";

    public GalleryWindowState() {
    }

    public GameRecord getCurrentRecord() {
        return currentRecord;
    }

    public void setCurrentRecord(GameRecord currentRecord) {
        this.currentRecord = currentRecord;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
