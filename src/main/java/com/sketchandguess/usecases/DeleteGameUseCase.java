package com.sketchandguess.usecases;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.entities.GameRecord;

public class DeleteGameUseCase {
    private final GameDataBase gameDataBase;
    public DeleteGameUseCase(GameDataBase gameDataBase) {
        this.gameDataBase = gameDataBase;
    }

    /**
     * @param gameRecord the game record which is supposed to be deleted
     * @return ture if the record is deleted successfully, false if failed to be deleted
     */
    public boolean delete (GameRecord gameRecord) {
        return gameDataBase.DeleteGame(gameRecord);
    }
}