package com.sketchandguess.usecases.gallery;

import com.sketchandguess.entities.GameRecord;

import java.util.List;

public class SearchGamesOutputData {
    public final String query;
    public final List<GameRecord> searchedGames;
    public SearchGamesOutputData(String query, List<GameRecord> searchedGames) {
        this.query = query;
        this.searchedGames = searchedGames;
    }
}