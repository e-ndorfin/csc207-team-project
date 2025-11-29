package com.sketchandguess.usecases.retrievegames;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.entities.GameRecord;

import java.util.ArrayList;

public class RetrieveGamesUseCase {
    private final GameDataBase GameData;
    public RetrieveGamesUseCase(GameDataBase GameData) {
        this.GameData = GameData;
    }
    public ArrayList<GameRecord> GetGames() {
        return this.GameData.GameData;
    }
    public ArrayList<GameRecord> SearchGames(String search) {
        return GameData.SearchWord(search).GameData;
    }

    public GameRecord GetGame(int GameCode) {
        return this.GameData.getGame(GameCode);
    }
}

