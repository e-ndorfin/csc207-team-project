package com.sketchandguess.usecases.deletegame;

import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.GameDataAccessInterface;

public class DeleteGameUseCase implements DeleteGameInputBoundary {
    private final GameDataAccessInterface gameDataAccess;
    private final DeleteGameOutputBoundary deleteGamePresenter;
    public DeleteGameUseCase(GameDataAccessInterface gameDataAccess, DeleteGameOutputBoundary deleteGamePresenter) {
        this.gameDataAccess = gameDataAccess;
        this.deleteGamePresenter = deleteGamePresenter;
    }

    @Override
    public void delete (GameRecord gameRecord) {
        try {
            boolean success = gameDataAccess.deleteGame(gameRecord);
            if  (success) {
                deleteGamePresenter.prepareDeleteSuccessView();
            } else {
                deleteGamePresenter.prepareFailView("Database delete returned false");
            }
        } catch (Exception e) {
            e.printStackTrace();
            deleteGamePresenter.prepareFailView(e.getMessage());
        }
    }
}

