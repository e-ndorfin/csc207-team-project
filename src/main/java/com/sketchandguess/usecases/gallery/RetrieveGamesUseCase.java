package com.sketchandguess.usecases.gallery;

import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.GameDataAccessInterface;

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
        List<GameRecord> outputData = this.dataAccessInterface.getGames();
        RetrieveGamesOutputData output = new RetrieveGamesOutputData(outputData);
        presenter.present(output);
    }
}
