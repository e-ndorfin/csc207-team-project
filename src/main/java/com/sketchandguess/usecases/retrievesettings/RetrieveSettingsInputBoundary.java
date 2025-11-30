package com.sketchandguess.usecases.retrievesettings;

/**
 * Input Boundary for RetrieveSettings use case.
 * Controller calls this interface to run the RetrieveSettings use case.
 */
public interface RetrieveSettingsInputBoundary {
    /**
     * Execute the RetrieveSettings use case with the given input data.
     *
     * @param inputData empty input data (no parameters needed for retrieval)
     */
    void execute(RetrieveSettingsInputData inputData);
}
