package com.sketchandguess.usecases.recordgame;

import java.time.LocalDate;

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

        // Build the GameRecord entity
        GameRecord record = new GameRecord(
                inputData.imagePath,
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
                inputData.imagePath,
                inputData.aiGuess
        );

        // Call presenter
        presenter.present(outputData);
    }
}

