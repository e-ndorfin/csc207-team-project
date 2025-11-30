package com.sketchandguess.usecases.deletegame;

import com.sketchandguess.entities.GameRecord;

public interface DeleteGameInputBoundary {
    /**
     * @param gameRecord the game record which is supposed to be deleted
     * @return true if the record is deleted successfully, false if failed to be deleted
     */
    boolean delete(GameRecord gameRecord);
}

