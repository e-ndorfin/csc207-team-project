package com.sketchandguess.usecases.recordgame;

/**
 * Output Boundary.
 * Use case calls this presenter after finishing the business logic.
 */
public interface RecordGameOutputBoundary {

    /**
     * Present the result of the RecordGame use case.
     *
     * @param outputData data prepared for the presenter / view model.
     */
    void present(RecordGameOutputData outputData);
}

