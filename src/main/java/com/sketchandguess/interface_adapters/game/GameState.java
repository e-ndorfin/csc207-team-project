package com.sketchandguess.interface_adapters.game;

import com.sketchandguess.entities.Prediction;
import java.util.ArrayList;
import java.util.List;

/**
 * Store the data need to show in game panel
 */
public class GameState {

    private String prompt = "";
    private String difficulty = "";
    private double timeLimitSeconds = 0;
    private double timeLeftSeconds = 0;
    private List<Prediction> predictions = new ArrayList<>();
    private boolean hasWon = false;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public double getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public void setTimeLimitSeconds(double timeLimitSeconds) {
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public double getTimeLeftSeconds() {
        return timeLeftSeconds;
    }

    public void setTimeLeftSeconds(double timeLeftSeconds) {
        this.timeLeftSeconds = timeLeftSeconds;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public boolean isHasWon() {
        return hasWon;
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }
}