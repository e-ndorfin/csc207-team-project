package com.sketchandguess.interface_adapters.settings;

import com.sketchandguess.interface_adapters.ViewModel;

public class SettingsViewModel extends ViewModel<SettingsState> {
    public static final String VIEW_NAME = "Settings";

    private SettingsState state = new SettingsState();

    public SettingsViewModel() {
        super(VIEW_NAME);
    }

    @Override
    public SettingsState getState() {
        return state;
    }

    @Override
    public void setState(SettingsState state) {
        this.state = state;
        firePropertyChange("state");
    }
}
