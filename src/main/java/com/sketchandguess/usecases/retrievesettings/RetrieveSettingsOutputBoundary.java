package com.sketchandguess.usecases.retrievesettings;

/**
 * Output Boundary for RetrieveSettings use case.
 * Use case calls this presenter after finishing the business logic.
 */
public interface RetrieveSettingsOutputBoundary {
    /**
     * Present the result of the RetrieveSettings use case.
     *
     * @param outputData the current settings retrieved from the database
     */
    void present(RetrieveSettingsOutputData outputData);
}
