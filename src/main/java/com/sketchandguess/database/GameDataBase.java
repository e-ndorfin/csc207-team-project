package com.sketchandguess.database;

import com.sketchandguess.entities.GameRecord;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GameDataBase implements DataBase {
    // The database storing each game.
    // The string contains the prompt used for the game, and helps us search for specific prompts.
    public ArrayList<GameRecord> GameData;

    public GameDataBase() {
        this.GameData = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("src\\main\\resources\\games.csv"));

            Iterator<String> iterator = lines.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] parts = line.split(",");
                GameRecord currentGame = new GameRecord(); // TODO: implement this once gameRecord constructor is implemented
                GameData.add(currentGame);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        ;
    }

    // overloaded version for search function
    public GameDataBase(ArrayList<GameRecord> GameData) {
        this.GameData = GameData;
    }

    public GameRecord GetGame(Integer GameCode) {
        return GameData.get(GameCode);
    }

    public GameDataBase SearchWord(String Query) {
        ArrayList<GameRecord> Matches = new ArrayList<>();
        for (GameRecord g: this.GameData) {
            // TODO come back to this once gamerecord is implemented
            if (g.getPrompt().contains(Query)) {
                Matches.add(Matches.size(), g);
            }
        }
        return new GameDataBase(Matches);
    }

    public Boolean DeleteGame(GameRecord DeletedGame) {
        try {
            this.GameData.remove(DeletedGame);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }

    }
}