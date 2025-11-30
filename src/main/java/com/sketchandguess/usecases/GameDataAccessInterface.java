package com.sketchandguess.usecases;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.entities.GameRecord;

import java.util.ArrayList;
import java.util.List;

public interface GameDataAccessInterface {
    List<GameRecord> getGames();
    GameRecord getGame(int gameCode);
    List<GameRecord> searchGames(String query);
    boolean deleteGame(GameRecord gameRecord);
    void save(GameRecord gameRecord);
}
