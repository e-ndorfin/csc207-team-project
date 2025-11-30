package com.sketchandguess.interface_adapters.gallery;

import com.sketchandguess.entities.GameRecord;

import java.util.ArrayList;
import java.util.List;

public class GalleryState {
    private List<GameRecord> gameRecords = new ArrayList<>();
    private String searchQuery = "";
    private boolean isEmpty = true;


    public List<GameRecord> getGameRecords() {
        return gameRecords;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setGameRecords(List<GameRecord> gameRecords) {
        this.gameRecords = gameRecords;
        if (gameRecords.isEmpty()) {
            isEmpty = true;
        } else  {
            isEmpty = false;
        }
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
