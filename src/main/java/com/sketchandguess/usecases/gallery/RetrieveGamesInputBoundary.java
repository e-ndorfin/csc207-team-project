package com.sketchandguess.usecases.gallery;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.entities.GameRecord;

import java.util.ArrayList;

public interface RetrieveGamesInputBoundary {
    void execute(RetrieveGamesInputData inputData);
}
