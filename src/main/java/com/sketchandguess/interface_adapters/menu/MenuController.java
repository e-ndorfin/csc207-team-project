package com.sketchandguess.interface_adapters.menu;

import com.sketchandguess.interface_adapters.ViewManagerModel;
import com.sketchandguess.interface_adapters.game.GameViewModel;
import com.sketchandguess.interface_adapters.settings.SettingsViewModel;

/**
 * Controller for the Main Menu view.
 * Handles user interactions and updates the ViewManagerModel to navigate between views.
 */
public class MenuController {

    private final ViewManagerModel viewManagerModel;

    public MenuController(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Navigate to the Game view.
     */
    public void executePlayGame() {
        viewManagerModel.setState(GameViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChange("view");
    }

    /**
     * Navigate to the Gallery view.
     */
    public void executeShowGallery() {
        viewManagerModel.setState("Gallery");
        viewManagerModel.firePropertyChange("view");
    }

    /**
     * Navigate to the Settings view.
     */
    public void executeShowSettings() {
        viewManagerModel.setState(SettingsViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChange("view");
    }

    /**
     * Exit the application.
     */
    public void executeExit() {
        System.exit(0);
    }
}
