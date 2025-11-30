package com.sketchandguess.interface_adapters.menu;

import com.sketchandguess.interface_adapters.ViewManagerModel;

public class MenuController {
    private final ViewManagerModel viewManagerModel;

    public MenuController(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public void startNewGame() {
        viewManagerModel.setState("Game");
        viewManagerModel.firePropertyChange("view");
    }

    public void showGallery() {
        viewManagerModel.setState("Gallery");
        viewManagerModel.firePropertyChange("view");
    }

    public void showSettings() {
        viewManagerModel.setState("Settings");
        viewManagerModel.firePropertyChange("view");
    }

    public void exitApplication() {
        System.exit(0);
    }
}
