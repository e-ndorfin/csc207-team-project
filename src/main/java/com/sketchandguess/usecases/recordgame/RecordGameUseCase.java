package com.sketchandguess.usecases.recordgame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;
import javax.imageio.ImageIO;

import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.usecases.GameDataAccessInterface;

/**
 * The RecordGame use case.
 * This class performs the business logic:
 *  - Build a GameRecord entity
 *  - Save it into the database (via GameDataAccessInterface)
 *  - Prepare output data
 *  - Call the presenter
 */
public class RecordGameUseCase implements RecordGameInputBoundary {

    /** Gateway to the game database. */
    private final GameDataAccessInterface gameDataAccessObject;

    /** Presenter that will receive the result. */
    private final RecordGameOutputBoundary presenter;

    public RecordGameUseCase(GameDataAccessInterface gameDataAccessObject,
                             RecordGameOutputBoundary presenter) {
        this.gameDataAccessObject = gameDataAccessObject;  // this is the gameDataBase
        this.presenter = presenter;
    }

    @Override
    public void execute(RecordGameInputData inputData) {

        // Save the full-size image and get its path
        String imagePath = saveImage(inputData.fullSizeImage);

        // Build the GameRecord entity
        GameRecord record = new GameRecord(
                imagePath,
                LocalDate.now(),
                inputData.hasWon,
                inputData.timeTaken,
                inputData.timeLimit,
                inputData.difficulty,
                inputData.prompt,
                inputData.aiGuess
        );

        // Save to database
        gameDataAccessObject.save(record);

        // Prepare output data for presenter / GameResult screen
        RecordGameOutputData outputData = new RecordGameOutputData(
                inputData.hasWon,
                inputData.prompt,
                inputData.timeTaken,
                inputData.timeLimit,
                record.getDateString(),
                inputData.difficulty,
                imagePath,
                inputData.aiGuess
        );

        // Call presenter
        presenter.present(outputData);
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

