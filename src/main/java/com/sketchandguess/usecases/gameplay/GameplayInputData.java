package com.sketchandguess.usecases.gameplay;

import java.awt.image.BufferedImage;

public class GameplayInputData {
    private final BufferedImage image;
    private final String targetPrompt;

    public GameplayInputData(BufferedImage image, String targetPrompt) {
        this.image = image;
        this.targetPrompt = targetPrompt;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getTargetPrompt() {
        return targetPrompt;
    }
}
