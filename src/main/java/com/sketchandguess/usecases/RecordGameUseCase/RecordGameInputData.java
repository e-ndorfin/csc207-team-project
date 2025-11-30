package com.sketchandguess.usecases.RecordGameUseCase;

import com.sketchandguess.entities.Difficulty;

/**
 * Data passed from Controller → Use Case.
 * Contains everything needed to create a GameRecord.
 */
public class RecordGameInputData {

    public final boolean hasWon;
    public final String prompt;
    public final double timeTaken;
    public final double timeLimit;
    public final Difficulty difficulty;
    public final String imagePath;
    public final String aiGuess;

    public RecordGameInputData(boolean hasWon,
                               String prompt,
                               double timeTaken,
                               double timeLimit,
                               Difficulty difficulty,
                               String imagePath,
                               String aiGuess) {
        this.hasWon = hasWon;
        this.prompt = prompt;
        this.timeTaken = timeTaken;
        this.timeLimit = timeLimit;
        this.difficulty = difficulty;
        this.imagePath = imagePath;
        this.aiGuess = aiGuess;
    }
}

