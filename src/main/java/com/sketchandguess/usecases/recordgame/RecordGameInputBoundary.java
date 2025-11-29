package com.sketchandguess.usecases.recordgame;

/**
 * Input Boundary.
 * Controller calls this interface to run the RecordGame use case.
 */
public interface RecordGameInputBoundary {

    /**
     * Execute the RecordGame use case with the given input data.
     *
     * @param inputData all information needed to record a game.
     */
    void execute(RecordGameInputData inputData);
}

