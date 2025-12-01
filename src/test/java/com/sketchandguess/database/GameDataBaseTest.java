package com.sketchandguess.database;

import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.entities.GameRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameDataBaseTest {

    private GameDataBase gameDataBase;

    @BeforeEach
    void setUp() {
        gameDataBase = new GameDataBase();
    }

    @Test
    void testSaveAddsToInMemoryList() {
        // Arrange
        int initialSize = gameDataBase.getGames().size();
        GameRecord newRecord = new GameRecord(
                "test_image.png",
                LocalDate.now(),
                true,
                10.0,
                60.0,
                new Difficulty("Easy"),
                "Test Prompt",
                "Test Guess"
        );

        // Act 
        gameDataBase.GameData.add(newRecord); 

        // Assert
        assertEquals(initialSize + 1, gameDataBase.getGames().size());
        assertEquals(newRecord, gameDataBase.getGames().get(initialSize));
    }

    @Test
    void testSearchGames() {
        // Arrange
        GameRecord record1 = new GameRecord("img1", LocalDate.now(), true, 10, 10, new Difficulty("Medium"), "Apple Pie", "Apple");
        GameRecord record2 = new GameRecord("img2", LocalDate.now(), true, 10, 10, new Difficulty("Medium"), "Banana Split", "Banana");
        
        gameDataBase.GameData.clear(); // Clear real data in memory
        gameDataBase.GameData.add(record1);
        gameDataBase.GameData.add(record2);

        // Act
        List<GameRecord> appleResults = gameDataBase.searchGames("Apple");
        List<GameRecord> splitResults = gameDataBase.searchGames("Split");
        List<GameRecord> noneResults = gameDataBase.searchGames("Orange");

        // Assert
        assertEquals(1, appleResults.size());
        assertEquals("Apple Pie", appleResults.get(0).getPrompt());

        assertEquals(1, splitResults.size());
        assertEquals("Banana Split", splitResults.get(0).getPrompt());

        assertEquals(0, noneResults.size());
    }
}
