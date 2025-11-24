package com.sketchandguess.usecases.select_game;

import com.sketchandguess.entities.GameRecord;

public interface SelectGameRecordInputBoundary {
    void execute(GameRecord record);
}
