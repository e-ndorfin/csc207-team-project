package com.sketchandguess.usecases.deletegame;

import com.sketchandguess.entities.GameRecord;

public interface DeleteGameInputBoundary {
    /**
     * @param gameRecord the game record which is supposed to be deleted
     * @return null if the record is deleted successfully, error message if failed
     */
    String delete(GameRecord gameRecord);
}
