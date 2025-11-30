package com.sketchandguess.interface_adapters.game;

public class GameResultState {
    private String prompt = "";
    private String aiGuess = "";
    private double timeTaken = 0.0;
    private double timeLimit = 0.0;
    private boolean hasWon = false;
    private String imagePath = "";
    private String endingMessage = "";

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getAiGuess() {
        return aiGuess;
    }

    public void setAiGuess(String aiGuess) {
        this.aiGuess = aiGuess;
    }

    public double getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(double timeTaken) {
        this.timeTaken = timeTaken;
    }

    public double getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(double timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isHasWon() {
        return hasWon;
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getEndingMessage() {
        return endingMessage;
    }

    public void setEndingMessage(String endingMessage) {
        this.endingMessage = endingMessage;
    }
}
