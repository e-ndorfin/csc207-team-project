package com.sketchandguess.entities;

import java.util.Random;

public class Difficulty {
    private String difficultyName;
    private String[] prompts;
    private int timeLimit;

    private static final String[] EASY_PROMPTS = {
        "apple", "arm", "axe", "banana", "bed", "bee", "belt", "book", "bread", "broom", 
        "bucket", "bush", "cactus", "cake", "calendar"
    };

    private static final String[] MEDIUM_PROMPTS = {
        "airplane", "ant", "anvil", "backpack", "bandage", "barn", "baseball", "basket", 
        "basketball", "bat", "bathtub", "bench", "bicycle", "bird", "blackberry", "blueberry", 
        "bottlecap", "bowtie", "brain", "broccoli", "bus", "butterfly", "calculator"
    };

    private static final String[] HARD_PROMPTS = {
        "ambulance", "angel", "asparagus", "beach", "bear", "beard", "binoculars", "boomerang", 
        "bracelet", "bridge", "bulldozer", "camel"
    };

    public Difficulty(String s) {
        if (!s.equals("Easy") && !s.equals("Medium") && !s.equals("Hard")) {
            s = "Medium";
            // throw new IllegalArgumentException("Difficulty must be Easy, Medium, or Hard");
        }
        this.difficultyName = s;
        
        switch (s) {
            case "Easy":
                this.prompts = EASY_PROMPTS;
                this.timeLimit = 60;
                break;
            case "Medium":
                this.prompts = MEDIUM_PROMPTS;
                this.timeLimit = 45;
                break;
            case "Hard":
                this.prompts = HARD_PROMPTS;
                this.timeLimit = 30;
                break;
        }
    }

    public String getDifficultyName() {
        return difficultyName;
    }

    public String getRandomPrompt() {
        Random rand = new Random();
        return prompts[rand.nextInt(prompts.length)];
    }

    public int getTimeLimit() {
        return timeLimit;
    }
}
