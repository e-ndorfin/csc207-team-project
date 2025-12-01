package com.sketchandguess.usecases.gallery;

import com.sketchandguess.entities.GameRecord;

import java.util.List;

public class RetrieveGamesOutputData {
    public final List<GameRecord> gameRecords;
    public RetrieveGamesOutputData(List<GameRecord> gameRecords) {
        this.gameRecords = gameRecords;
    }
}