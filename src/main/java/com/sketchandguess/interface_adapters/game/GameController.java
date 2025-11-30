package com.sketchandguess.interface_adapters.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.usecases.recordgame.RecordGameInputBoundary;
import com.sketchandguess.usecases.recordgame.RecordGameInputData;
import com.sketchandguess.usecases.gameplay.GameplayInputBoundary;
import com.sketchandguess.usecases.gameplay.GameplayInputData;

public class GameController {

    private final RecordGameInputBoundary recordGameInteractor;
    private final GameplayInputBoundary gameplayInteractor;

    public GameController(RecordGameInputBoundary recordGameInteractor, GameplayInputBoundary gameplayInteractor) {
        this.recordGameInteractor = recordGameInteractor;
        this.gameplayInteractor = gameplayInteractor;
    }

    public void checkPrediction(BufferedImage image, String prompt) {
        GameplayInputData inputData = new GameplayInputData(image, prompt);
        gameplayInteractor.execute(inputData);
    }

    /**
     * Executes the game result processing:
     * 1. Saves the drawing to disk.
     * 2. Packages the game stats.
     * 3. Calls the RecordGame use case.
     *
     * @param image      The drawing from the game.
     * @param prompt     The prompt for the game.
     * @param difficulty The difficulty level.
     * @param timeTaken  The time taken by the user.
     * @param timeLimit  The total time limit.
     * @param hasWon     Whether the user won the game.
     */
    public void executeGameResult(BufferedImage fullSizeImage, BufferedImage downsampledImage, String prompt, Difficulty difficulty, double timeTaken, double timeLimit, boolean hasWon) {
        // AI guess placeholder - this is now handled by gameplay loop, but we need it for record
        String aiGuess = hasWon ? prompt : "Incorrect"; // Simple placeholder for record

        RecordGameInputData inputData = new RecordGameInputData(
                hasWon,
                prompt,
                timeTaken,
                timeLimit,
                difficulty,
                fullSizeImage,
                downsampledImage,
                aiGuess
        );

        // Execute use case
        recordGameInteractor.execute(inputData);
    }

    
}
