package com.sketchandguess.interface_adapters.gallery_window;

import com.sketchandguess.entities.GameRecord;

public class GalleryWindowState {
    private GameRecord currentRecord;

    public GameRecord getCurrentRecord() {
        return currentRecord;
    }

    public void setCurrentRecord(GameRecord currentRecord) {
        this.currentRecord = currentRecord;
    }
}