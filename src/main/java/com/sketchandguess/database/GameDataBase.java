package com.sketchandguess.database;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.GameDataAccessInterface;

public class GameDataBase implements GameDataAccessInterface {
    // The database storing each game.
    // The string contains the prompt used for the game, and helps us search for specific prompts.
    public ArrayList<GameRecord> GameData;
    private final String csvPath = "src/main/resources/games.csv";

    public GameDataBase() {
        this.GameData = new ArrayList<>();
        // Read from the CSV file if it exists - if doesn't exist, 
        // then it will initialize it in the save() function below
        try {  
            java.nio.file.Path path = Paths.get(csvPath);
            if (Files.exists(path)) {
                List<String> lines = Files.readAllLines(path);
                for (String line : lines) {
                    if (line.trim().isEmpty()) continue; // Skip empty lines
                    ArrayList<String> parts = new ArrayList<>(Arrays.asList(line.split(",")));
                    GameRecord currentGame = ConvertToRecord(parts);
                    GameData.add(currentGame);
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // overloaded version for search function
    public GameDataBase(ArrayList<GameRecord> GameData) {
        this.GameData = GameData;
    }

    @Override
    public GameRecord getGame(int gameCode) {
        if (gameCode >= 0 && gameCode < GameData.size()) {
            return GameData.get(gameCode);
        }
        return null;
    }

    @Override
    public List<GameRecord> getGames() {
        return new ArrayList<>(GameData);
    }

    @Override
    public List<GameRecord> searchGames(String query) {
        ArrayList<GameRecord> matches = new ArrayList<>();
        for (GameRecord g : this.GameData) {
            if (g.getPrompt().contains(query)) {
                matches.add(g);
            }
        }
        return matches;
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
        String aiGuess = "N/A";
        if (data.size() > 7) {
            aiGuess = data.get(7);
        }
        
        return new GameRecord(data.get(0),
                LocalDate.parse(data.get(1)),
                Boolean.parseBoolean(data.get(2)),
                Double.parseDouble(data.get(3)),
                Double.parseDouble(data.get(4)),
                new Difficulty(data.get(5)),
                data.get(6),
                aiGuess
        );
    }
    
    @Override
    public boolean deleteGame(GameRecord deletedGame) {
        try {
            // Remove from GameData list
            boolean removed = this.GameData.remove(deletedGame);
            
            if (removed) {
                // Rewrite the entire CSV file with remaining games from GameData
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath, false))) {
                    for (GameRecord game : this.GameData) {
                        // Format: imagePath,date,hasWon,timeTaken,timeLimit,difficulty,prompt,aiGuess
                        String line = String.format("%s,%s,%b,%.2f,%.2f,%s,%s,%s",
                                game.getImagePath(),
                                game.getDate().toString(),
                                game.getHasWon(),
                                game.getTimeTaken(),
                                game.getTimeLimit(),
                                game.getDifficulty().getDifficultyName(),
                                game.getPrompt(),
                                game.getAiGuess()
                        );
                        
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
            
            return removed;
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete game record from CSV", e);
        }
    }

    public Boolean DeleteGame(GameRecord DeletedGame) {
        try {
            this.GameData.remove(DeletedGame);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }

    }

    @Override
    public void save(GameRecord gameRecord) {
        this.GameData.add(gameRecord);
        
        // Append to CSV or create if it doesn't exist
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath, true))) {
            // Format: imagePath,date,hasWon,timeTaken,timeLimit,difficulty,prompt,aiGuess
            String line = String.format("%s,%s,%b,%.2f,%.2f,%s,%s,%s",
                    gameRecord.getImagePath(),
                    gameRecord.getDate().toString(), // Use default ISO format (YYYY-MM-DD) for parsing
                    gameRecord.getHasWon(),
                    gameRecord.getTimeTaken(),
                    gameRecord.getTimeLimit(),
                    gameRecord.getDifficulty().getDifficultyName(),
                    gameRecord.getPrompt(),
                    gameRecord.getAiGuess()
            );
            
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save game record to CSV", e);
        }
    }
}
