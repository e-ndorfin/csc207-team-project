package com.sketchandguess.usecases.gallery;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.usecases.GameDataAccessInterface;

import java.util.ArrayList;

public class SearchGamesUseCase implements SearchGamesInputBoundary {
    private final GameDataAccessInterface gameDataAccess;
    private final SearchGamesOutputBoundary presenter;
    public SearchGamesUseCase(GameDataAccessInterface gameDataAccess, SearchGamesOutputBoundary presenter) {
        this.gameDataAccess = gameDataAccess;
        this.presenter = presenter;
    }
    @Override
    public void execute(SearchGamesInputData input) {
        SearchGamesOutputData outputData = new SearchGamesOutputData(input.query, gameDataAccess.searchGames(input.query));
        presenter.present(outputData);
    }
}
