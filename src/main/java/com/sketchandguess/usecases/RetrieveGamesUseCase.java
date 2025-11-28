package com.sketchandguess.usecases;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.database.SearchObject;
import com.sketchandguess.entities.GameRecord;

import java.util.ArrayList;
import java.util.List;

public class RetrieveGamesUseCase {
    private final GameDataBase dataBase;

    public RetrieveGamesUseCase(GameDataBase dataBase) {
        this.dataBase = dataBase;
    }

    public List<GameRecord> searchGames(SearchObject search) {
            ArrayList<GameRecord> gameMatches = new ArrayList<>();
            for (GameRecord g: this.dataBase.getGameData()) {
                if (g.getPrompt().contains(search.getQuery())) {
                    gameMatches.add(gameMatches.size(), g);
                }
            }
            return gameMatches;
        }

}