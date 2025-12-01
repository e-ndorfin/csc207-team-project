package com.sketchandguess.usecases.deletegame;

import com.sketchandguess.entities.GameRecord;

public interface DeleteGameInputBoundary {
    void delete(GameRecord gameRecord);
}