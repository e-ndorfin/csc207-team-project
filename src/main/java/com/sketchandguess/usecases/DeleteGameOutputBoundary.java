package com.sketchandguess.usecases;

public interface DeleteGameOutputBoundary {
    void prepareDeleteSuccessView();
    void prepareFailView(String error);
}