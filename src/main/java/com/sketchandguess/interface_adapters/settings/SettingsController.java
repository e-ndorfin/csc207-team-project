package com.sketchandguess.interface_adapters.settings;

import com.sketchandguess.usecases.editsettings.EditSettingsInputBoundary;
import com.sketchandguess.usecases.editsettings.EditSettingsInputData;
import com.sketchandguess.usecases.retrievesettings.RetrieveSettingsInputBoundary;
import com.sketchandguess.usecases.retrievesettings.RetrieveSettingsInputData;

public class SettingsController {
    
    private final EditSettingsInputBoundary editSettingsInteractor;
    private final RetrieveSettingsInputBoundary retrieveSettingsInteractor;

    public SettingsController(EditSettingsInputBoundary editSettingsInteractor, 
                            RetrieveSettingsInputBoundary retrieveSettingsInteractor) {
        this.editSettingsInteractor = editSettingsInteractor;
        this.retrieveSettingsInteractor = retrieveSettingsInteractor;
    }

    /**
     * Execute the edit settings use case with the given time limit and difficulty.
     *
     * @param timeLimitInSeconds the new time limit in seconds
     * @param difficultyName the new difficulty ("easy", "medium", "hard")
     */
    public void executeEditSettings(int timeLimitInSeconds, String difficultyName) {
        EditSettingsInputData inputData = new EditSettingsInputData(timeLimitInSeconds, difficultyName);
        editSettingsInteractor.execute(inputData);
    }

    /**
     * Execute the retrieve settings use case.
     * No parameters needed for retrieval.
     */
    public void executeRetrieveSettings() {
        RetrieveSettingsInputData inputData = new RetrieveSettingsInputData();
        retrieveSettingsInteractor.execute(inputData);
    }
}
