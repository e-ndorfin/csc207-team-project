package com.sketchandguess.database;

import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.RetrieveGamesUseCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class GameDataBase implements DataBase {
    // The database storing each game.
    // The string contains the prompt used for the game, and helps us search for specific prompts.
    private final List<GameRecord> gameData;

    public GameDataBase() {
        this.gameData = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("src", "main", "resources", "games.csv"));

            for (String line : lines) {
                ArrayList<String> parts = new ArrayList<>(Arrays.asList(line.split(",")));
                GameRecord currentGame = convertToRecord(parts);
                gameData.add(currentGame);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    public List<GameRecord> getGameData() {
        return this.gameData;
    }

    // overloaded version for search function
    public GameDataBase(List<GameRecord> gameData) {
        this.gameData = gameData;
    }



    // TODO: gallery search function creates a new "search" object. implement the search object, then make it so this function takes the search object as input
    public GameDataBase SearchWord(SearchObject searchQuery) {
        RetrieveGamesUseCase gameSearcher = new RetrieveGamesUseCase(this);
        return new GameDataBase(gameSearcher.searchGames(searchQuery));
    }

    private GameRecord convertToRecord(ArrayList<String> data) {
        return new GameRecord(data.get(0),
        LocalDate.parse(data.get(1)),
        Boolean.parseBoolean(data.get(2)),
        Double.parseDouble(data.get(3)),
        Double.parseDouble(data.get(4)),
                        new Difficulty(data.get(5)),
                                data.get(6)

        );
    }
    public Boolean DeleteGame(GameRecord deletedGame) {
        try {
            this.gameData.remove(deletedGame);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        }
    }
