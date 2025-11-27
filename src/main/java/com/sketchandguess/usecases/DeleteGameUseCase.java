package com.sketchandguess.usecases;

// import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.entities.GameRecord;

public class DeleteGameUseCase implements DeleteGameInputBoundary {
    private final GameDataAccessInterface gameDataAccess;
    public DeleteGameUseCase(GameDataAccessInterface gameDataAccess) {
        this.gameDataAccess = gameDataAccess;
    }

    /**
     * @param gameRecord the game record which is supposed to be deleted
     * @return null if the record is deleted successfully, error message if failed
     */
    @Override
    public String delete (GameRecord gameRecord) {
        try {
            boolean success = gameDataAccess.deleteGame(gameRecord);
            return success ? null : "Database delete returned false";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}