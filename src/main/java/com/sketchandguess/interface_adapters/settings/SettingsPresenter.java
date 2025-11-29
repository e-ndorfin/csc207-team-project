package com.sketchandguess.interface_adapters.settings;

import com.sketchandguess.usecases.editsettings.EditSettingsOutputBoundary;
import com.sketchandguess.usecases.editsettings.EditSettingsOutputData;
import com.sketchandguess.usecases.retrievesettings.RetrieveSettingsOutputBoundary;
import com.sketchandguess.usecases.retrievesettings.RetrieveSettingsOutputData;

public class SettingsPresenter implements EditSettingsOutputBoundary, RetrieveSettingsOutputBoundary {
    
    private final SettingsViewModel viewModel;

    public SettingsPresenter(SettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(EditSettingsOutputData outputData) {
        SettingsState state = viewModel.getState();
        
        if (outputData.success) {
            // Success case
            state.setDefaultTimeLimit(outputData.defaultTimeLimit);
            state.setDifficultyName(outputData.difficultyName);
            state.setSuccessMessage("Settings updated successfully.");
            state.setErrorMessage("");
        } else {
            // Error case - keep current settings but show error
            state.setErrorMessage(outputData.errorMessage);
            state.setSuccessMessage("");
        }
        
        viewModel.setState(state);
        viewModel.firePropertyChange("state");
    }

    @Override
    public void present(RetrieveSettingsOutputData outputData) {
        SettingsState state = viewModel.getState();
        
        // Update with retrieved settings
        state.setDefaultTimeLimit(outputData.defaultTimeLimit);
        state.setDifficultyName(outputData.difficultyName);
        state.setSuccessMessage("");
        state.setErrorMessage("");
        
        viewModel.setState(state);
        viewModel.firePropertyChange("state");
    }
}
