package com.sketchandguess.usecases.gallery;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.database.SearchObject;
import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.GameDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class RetrieveGamesUseCase implements RetrieveGamesInputBoundary {
    private final GameDataAccessInterface dataAccessInterface;
    private final RetrieveGamesOutputBoundary presenter;
    public RetrieveGamesUseCase(GameDataAccessInterface dataAccessInterface, RetrieveGamesOutputBoundary presenter) {
        this.dataAccessInterface = dataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute(RetrieveGamesInputData inputData) {
        GameDataBase gameDataBase = new GameDataBase();
        ArrayList<GameRecord> outputData = this.dataAccessInterface.getGames(gameDataBase);
        RetrieveGamesOutputData output = new RetrieveGamesOutputData(outputData);
        presenter.present(output);

    }
}