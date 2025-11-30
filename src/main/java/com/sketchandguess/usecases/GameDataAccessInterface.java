package com.sketchandguess.usecases;

import com.sketchandguess.entities.GameRecord;
import java.util.List;

public interface GameDataAccessInterface {
    List<GameRecord> getGames();
    List<GameRecord> searchGames(String query);
    GameRecord getGame(int gameCode);
    boolean deleteGame(GameRecord gameRecord);
    void save(GameRecord gameRecord);
}

