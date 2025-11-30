package com.sketchandguess.usecases.gallery;

import com.sketchandguess.entities.GameRecord;

import java.util.ArrayList;

public class RetrieveGamesOutputData {
    public final ArrayList<GameRecord> gameRecords;
    public RetrieveGamesOutputData(ArrayList<GameRecord> gameRecords) {
        this.gameRecords = gameRecords;
    }
}
