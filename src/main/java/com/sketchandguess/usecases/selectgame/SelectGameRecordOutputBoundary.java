package com.sketchandguess.usecases.selectgame;

import com.sketchandguess.entities.GameRecord;

public interface SelectGameRecordOutputBoundary {
    void prepareSuccessView(GameRecord record);
    void prepareFailView(String error);
}

