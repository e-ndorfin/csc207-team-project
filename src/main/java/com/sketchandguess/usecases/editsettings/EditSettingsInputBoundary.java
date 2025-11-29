package com.sketchandguess.usecases.editsettings;

/**
 * Input Boundary for EditSettings use case.
 * Controller calls this interface to run the EditSettings use case.
 */
public interface EditSettingsInputBoundary {
    /**
     * Execute the EditSettings use case with the given input data.
     *
     * @param inputData the time limit and difficulty to set
     */
    void execute(EditSettingsInputData inputData);
}
