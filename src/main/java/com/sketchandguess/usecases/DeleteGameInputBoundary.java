package com.sketchandguess.usecases;

import com.sketchandguess.entities.GameRecord;

public interface DeleteGameInputBoundary {
    void delete(GameRecord gameRecord);
}