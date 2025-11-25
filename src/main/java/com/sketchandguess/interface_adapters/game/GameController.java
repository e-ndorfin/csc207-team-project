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
import com.sketchandguess.usecases.RecordGameUseCase.RecordGameInputBoundary;
import com.sketchandguess.usecases.RecordGameUseCase.RecordGameInputData;

public class GameController {

    private final RecordGameInputBoundary recordGameInteractor;

    public GameController(RecordGameInputBoundary recordGameInteractor) {
        this.recordGameInteractor = recordGameInteractor;
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
     */
    public void executeGameResult(BufferedImage image, String prompt, Difficulty difficulty, double timeTaken, double timeLimit) {
        String imagePath = saveImage(image);

        // AI guess placeholder
        String aiGuess = "I'm still learning..."; // TODO: Integrate AI call here
        boolean hasWon = true; // TODO: Determine win/loss based on AI guess

        RecordGameInputData inputData = new RecordGameInputData(
                hasWon,
                prompt,
                timeTaken,
                timeLimit,
                difficulty,
                imagePath,
                aiGuess
        );

        // Execute use case
        recordGameInteractor.execute(inputData);
    }

    private String saveImage(BufferedImage image) {
        String directory = "src/main/resources/images/";
        String fileName = UUID.randomUUID().toString() + ".png";
        Path path = Paths.get(directory + fileName);

        try {
            Files.createDirectories(path.getParent());
            File outputFile = path.toFile();
            ImageIO.write(image, "png", outputFile);
            return directory + fileName; 
        } catch (IOException e) {
            e.printStackTrace();
            return "default_image.png"; 
        }
    }
}
