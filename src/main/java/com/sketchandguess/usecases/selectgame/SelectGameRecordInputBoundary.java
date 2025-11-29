package com.sketchandguess.usecases.selectgame;

import com.sketchandguess.entities.GameRecord;

public interface SelectGameRecordInputBoundary {
    void execute(GameRecord record);
}

