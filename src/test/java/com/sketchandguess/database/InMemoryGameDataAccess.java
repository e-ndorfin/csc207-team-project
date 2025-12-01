package com.sketchandguess.database;

import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.entities.GameRecord;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation that extends GameDataBase for testing purposes. 
 * Stores GameRecords in memory and provides basic CRUD operations without CSV file I/O.
 */
public class InMemoryGameDataAccess extends GameDataBase {
    
    public InMemoryGameDataAccess() {
        // Use the overloaded constructor that takes ArrayList to avoid CSV loading
        super(new ArrayList<>());
    }
    
    @Override
    public List<GameRecord> getGames() {
        return new ArrayList<>(GameData);
    }
    
    @Override
    public List<GameRecord> searchGames(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getGames();
        }
        
        String lowerQuery = query.toLowerCase().trim();
        ArrayList<GameRecord> filteredGames = new ArrayList<>();
        
        for (GameRecord game : GameData) {
            if (game.getPrompt().toLowerCase().contains(lowerQuery)) {
                filteredGames.add(game);
            }
        }
        
        return filteredGames;
    }
    
    @Override
    public GameRecord getGame(int gameCode) {
        if (gameCode >= 0 && gameCode < GameData.size()) {
            return GameData.get(gameCode);
        }
        return null;
    }
    
    @Override
    public boolean deleteGame(GameRecord gameRecord) {
        if (gameRecord != null && GameData.contains(gameRecord)) {
            return GameData.remove(gameRecord);
        }
        return false;
    }
    
    @Override
    public void save(GameRecord gameRecord) {
        if (gameRecord != null) {
            // If the game already exists (same image path), update it
            for (int i = 0; i < GameData.size(); i++) {
                if (GameData.get(i).getImagePath().equals(gameRecord.getImagePath())) {
                    // Remove old and add new (since GameRecord is immutable)
                    GameData.remove(i);
                    GameData.add(i, gameRecord);
                    return;
                }
            }
            
            // New game - add to list (without CSV writing)
            GameData.add(gameRecord);
        }
    }
    
    // Helper method for testing - add a test game
    public void addTestGame(GameRecord game) {
        if (game != null) {
            // Check if game with same image path already exists
            for (int i = 0; i < GameData.size(); i++) {
                if (GameData.get(i).getImagePath().equals(game.getImagePath())) {
                    // Update by creating new GameRecord with updated values
                    GameRecord updatedGame = new GameRecord(
                        game.getImagePath(),
                        game.getDate(),
                        game.getHasWon(),
                        game.getTimeTaken(),
                        game.getTimeLimit(),
                        game.getDifficulty(),
                        game.getPrompt(),
                        game.getAiGuess()
                    );
                    GameData.set(i, updatedGame);
                    return;
                }
            }
            // Add new
            GameData.add(game);
        }
    }
    
    // Helper method for testing - clear all games
    public void clearGames() {
        GameData.clear();
    }
    
    // Helper method to create a test GameRecord for convenience
    public static GameRecord createTestGameRecord(String imagePath, boolean hasWon, String prompt, String aiGuess) {
        Difficulty difficulty = new Difficulty("Medium");
        return new GameRecord(
            imagePath,
            LocalDate.now(),
            hasWon,
            45.5,
            60.0,
            difficulty,
            prompt,
            aiGuess
        );
    }
}
