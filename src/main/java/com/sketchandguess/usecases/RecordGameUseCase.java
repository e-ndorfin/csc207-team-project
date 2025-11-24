package com.sketchandguess.usecases;

import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.database.GameDataBase;

import java.time.LocalDate;

/**
 * The RecordGame use case.
 * This class performs the business logic:
 *  - Build a GameRecord entity
 *  - Save it into the database (via GameDataBase)
 *  - Prepare output data
 *  - Call the presenter
 */
public class RecordGameUseCase implements RecordGameInputBoundary {

    private final GameDataBase gameDataBase;          // gateway to database
    private final RecordGameOutputBoundary presenter; // presenter

    public RecordGameUseCase(GameDataBase gameDataBase,
                             RecordGameOutputBoundary presenter) {
        this.gameDataBase = gameDataBase;
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
                inputData.prompt
        );

        // Save to database
        gameDataBase.GameData.add(record);

        // Prepare output data
        RecordGameOutputData outputData = new RecordGameOutputData(
                inputData.hasWon,
                inputData.prompt,
                inputData.timeTaken,
                record.getDateString()
        );

        // Call presenter
        presenter.present(outputData);
    }
}

/**
 * Input Boundary
 * Controller calls this use case.
 */
interface RecordGameInputBoundary {
    void execute(RecordGameInputData inputData);
}

/**
 * Output Boundary
 * Use case calls this presenter.
 */
interface RecordGameOutputBoundary {
    void present(RecordGameOutputData outputData);
}

/**
 * Data passed from Controller → UseCase.
 */
class RecordGameInputData {
    public final boolean hasWon;
    public final String prompt;
    public final double timeTaken;
    public final double timeLimit;
    public final Difficulty difficulty;
    public final String imagePath;

    public RecordGameInputData(boolean hasWon,
                               String prompt,
                               double timeTaken,
                               double timeLimit,
                               Difficulty difficulty,
                               String imagePath) {
        this.hasWon = hasWon;
        this.prompt = prompt;
        this.timeTaken = timeTaken;
        this.timeLimit = timeLimit;
        this.difficulty = difficulty;
        this.imagePath = imagePath;
    }
}

/**
 * Data returned UseCase → Presenter.
 */
class RecordGameOutputData {
    public final boolean hasWon;
    public final String prompt;
    public final double timeTaken;
    public final String date;

    public RecordGameOutputData(boolean hasWon,
                                String prompt,
                                double timeTaken,
                                String date) {
        this.hasWon = hasWon;
        this.prompt = prompt;
        this.timeTaken = timeTaken;
        this.date = date;
    }
}
