package com.sketchandguess.usecases.RecordGameUseCase;

import com.sketchandguess.entities.Difficulty;

/**
 * Data returned from Use Case → Presenter.
 * Presenter/ViewModel will use this to update the GameResult screen.
 */
public class RecordGameOutputData {

    public final boolean hasWon;
    public final String prompt;
    public final double timeTaken;
    public final double timeLimit;
    public final String date;
    public final Difficulty difficulty;
    public final String imagePath;
    public final String aiGuess;

    public RecordGameOutputData(boolean hasWon,
                                String prompt,
                                double timeTaken,
                                double timeLimit,
                                String date,
                                Difficulty difficulty,
                                String imagePath,
                                String aiGuess) {
        this.hasWon = hasWon;
        this.prompt = prompt;
        this.timeTaken = timeTaken;
        this.timeLimit = timeLimit;
        this.date = date;
        this.difficulty = difficulty;
        this.imagePath = imagePath;
        this.aiGuess = aiGuess;
    }
}

