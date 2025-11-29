package com.sketchandguess.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GameRecord {
    private String imagePath;
    private LocalDate date;
    private boolean hasWon;
    private double timeTaken;
    private double timeLimit;
    private Difficulty difficulty;
    private String prompt;
    private String aiGuess;

    public GameRecord(String imagePath,
                      LocalDate date,
                      boolean hasWon,
                      double timeTaken,
                      double timeLimit,
                      Difficulty difficulty,
                      String prompt,
                      String aiGuess) {
        this.imagePath = imagePath;
        this.date = date;
        this.hasWon = hasWon;
        this.timeTaken = timeTaken;
        this.timeLimit = timeLimit;
        this.difficulty = difficulty;
        this.prompt = prompt;
        this.aiGuess = aiGuess;
    }

    /**
     * Returns where the image stored in our database.
     * @return where the image stored in our database.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Returns the current local date.
     * @return the current local date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the current local date as a string.
     * @return the current local date as a string.
     */
    public String getDateString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return  dateFormatter.format(date);
    }

    /**
     * Returns if the user has won.
     * @return if the user has won.
     */
    public boolean getHasWon() {
        return hasWon;
    }

    /**
     * Returns the time user took.
     * @return the time user took.
     */
    public double getTimeTaken() {
        return timeTaken;
    }

    /**
     * Returns the time limit of the game.
     * @return the time limit of the game.
     */
    public double getTimeLimit() {
        return timeLimit;
    }

    /**
     * Returns the difficulty of the game.
     * @return the difficulty of the game.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the prompt of the game.
     * @return the prompt of the game.
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Returns the AI's guess.
     * @return the AI's guess.
     */
    public String getAiGuess() {
        return aiGuess;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GameRecord gameRecord = (GameRecord) object;
        if (!imagePath.equals(gameRecord.imagePath)) return false;
        if (!date.equals(gameRecord.date)) return false;
        return prompt.equals(gameRecord.prompt);
    }

    public int hashCode() {
        int result = imagePath.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + prompt.hashCode();
        return result;
    }
}