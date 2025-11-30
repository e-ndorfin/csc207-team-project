package com.sketchandguess.usecases.gallery;

import com.sketchandguess.entities.GameRecord;

import java.util.ArrayList;

public class SearchGamesOutputData {
    public final String query;
    public final ArrayList<GameRecord> searchedGames;
    public SearchGamesOutputData(String query, ArrayList<GameRecord> searchedGames) {
        this.query = query;
        this.searchedGames = searchedGames;
    }
}
