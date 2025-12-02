package com.sketchandguess.usecases.deletegame;

public interface DeleteGameOutputBoundary {
    void prepareDeleteSuccessView();
    void prepareFailView(String error);
}