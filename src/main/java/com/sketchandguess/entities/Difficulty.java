package com.sketchandguess.entities;

public class Difficulty {
    private String difficultyName;
    private String[] prompts;

    public Difficulty(String s) {
        if (!s.equals("Easy") && !s.equals("Medium") && !s.equals("Hard")) {
            throw new IllegalArgumentException("Difficulty must be Easy, Medium, or Hard");
        }
        this.difficultyName = s;
    }

    public String getDifficultyName() {
        return difficultyName;
    }
}