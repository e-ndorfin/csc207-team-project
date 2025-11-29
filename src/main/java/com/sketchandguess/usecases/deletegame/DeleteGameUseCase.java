package com.sketchandguess.usecases.deletegame;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.entities.GameRecord;

public class DeleteGameUseCase implements DeleteGameInputBoundary {
    private final GameDataBase gameDataBase;
    public DeleteGameUseCase(GameDataBase gameDataBase) {
        this.gameDataBase = gameDataBase;
    }

    /**
     * @param gameRecord the game record which is supposed to be deleted
     * @return ture if the record is deleted successfully, false if failed to be deleted
     */
    @Override
    public boolean delete (GameRecord gameRecord) {
        return gameDataBase.DeleteGame(gameRecord);
    }
}

