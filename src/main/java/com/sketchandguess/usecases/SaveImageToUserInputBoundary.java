package com.sketchandguess.usecases;

import java.io.IOException;

public interface SaveImageToUserInputBoundary {
    /**
     * @param imagePath the path where the gallery save the image
     * @return true if saved, false if failed to save the image
     */
    boolean save(String imagePath) throws IOException;
}