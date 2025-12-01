package com.sketchandguess.usecases.recordgame;

import com.sketchandguess.entities.Difficulty;
import java.awt.image.BufferedImage;

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
    public final BufferedImage fullSizeImage;
    public final BufferedImage downsampledImage;
    public final String aiGuess;

    public RecordGameInputData(boolean hasWon,
                               String prompt,
                               double timeTaken,
                               double timeLimit,
                               Difficulty difficulty,
                               BufferedImage fullSizeImage,
                               BufferedImage downsampledImage,
                               String aiGuess) {
        this.hasWon = hasWon;
        this.prompt = prompt;
        this.timeTaken = timeTaken;
        this.timeLimit = timeLimit;
        this.difficulty = difficulty;
        this.fullSizeImage = fullSizeImage;
        this.downsampledImage = downsampledImage;
        this.aiGuess = aiGuess;
    }
}

