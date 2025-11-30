package com.sketchandguess.interface_adapters.menu;

import com.sketchandguess.interface_adapters.ViewModel;

public class MenuViewModel extends ViewModel<MenuState> {

    public static final String VIEW_NAME = "MainMenu";

    private MenuState state = new MenuState();

    public MenuViewModel() {
        super(VIEW_NAME);
    }

    @Override
    public MenuState getState() {
        return state;
    }

    @Override
    public void setState(MenuState state) {
        this.state = state;
        super.setState(state);
        firePropertyChange("state");
    }
}
