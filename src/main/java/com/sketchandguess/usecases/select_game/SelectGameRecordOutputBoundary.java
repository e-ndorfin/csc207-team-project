package com.sketchandguess.usecases.select_game;

import com.sketchandguess.entities.GameRecord;

public interface SelectGameRecordOutputBoundary {
    void prepareSuccessView(GameRecord record);
    void prepareFailView(String error);
}
