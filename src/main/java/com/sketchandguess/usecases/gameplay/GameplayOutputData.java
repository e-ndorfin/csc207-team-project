package com.sketchandguess.usecases.gameplay;

import com.sketchandguess.entities.Prediction;
import java.util.List;

public class GameplayOutputData {
    private final List<Prediction> predictions;
    private final boolean hasWon;

    public GameplayOutputData(List<Prediction> predictions, boolean hasWon) {
        this.predictions = predictions;
        this.hasWon = hasWon;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public boolean hasWon() {
        return hasWon;
    }
}
