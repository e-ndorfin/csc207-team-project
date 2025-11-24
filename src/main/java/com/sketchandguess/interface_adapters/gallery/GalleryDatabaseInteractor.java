package com.sketchandguess.interface_adapters.gallery;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.database.SearchObject;
import com.sketchandguess.gui.Gallery;

import java.util.List;

public class GalleryDatabaseInteractor {
    GameDataBase db;

    public GalleryDatabaseInteractor() {
        this.db = new GameDataBase();
    }

    public void SearchDB(SearchObject query) {
        this.db = this.db.SearchWord(query);
    }

    public boolean isEmpty() {
        return this.db.getGameData().isEmpty();
    }
}
