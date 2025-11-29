package com.sketchandguess.usecases.editsettings;

/**
 * Output Boundary for EditSettings use case.
 * Use case calls this presenter after finishing the business logic.
 */
public interface EditSettingsOutputBoundary {
    /**
     * Present the result of the EditSettings use case.
     *
     * @param outputData the result of the settings update (success/error and current settings)
     */
    void present(EditSettingsOutputData outputData);
}
