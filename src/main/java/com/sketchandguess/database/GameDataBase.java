package com.sketchandguess.database;

import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.entities.GameRecord;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class GameDataBase implements DataBase {
    // The database storing each game.
    // The string contains the prompt used for the game, and helps us search for specific prompts.
    public ArrayList<GameRecord> GameData;

    public GameDataBase() {
        this.GameData = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("src", "main", "resources", "games.csv"));

            Iterator<String> iterator = lines.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                ArrayList<String> parts = new ArrayList<>(Arrays.asList(line.split(",")));
                GameRecord currentGame = ConvertToRecord(parts);
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

    private GameRecord ConvertToRecord(ArrayList<String> data) {
        return new GameRecord(data.get(0),
        LocalDate.parse(data.get(1)),
        Boolean.parseBoolean(data.get(2)),
        Double.parseDouble(data.get(3)),
        Double.parseDouble(data.get(4)),
                        new Difficulty(data.get(5)),
                                data.get(6),
                data.get(7)

        );
    }
    public Boolean DeleteGame(GameRecord DeletedGame) {
        try {
            this.GameData.remove(DeletedGame);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        }

    public ArrayList<GameRecord> getGameData() {
        return this.GameData;
    }
}
