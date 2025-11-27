package com.sketchandguess.interface_adapters.game;

import com.sketchandguess.interface_adapters.ViewModel;
public class GameResultViewModel extends ViewModel {
    public static final String VIEW_NAME = "GameResult";

    private GameResultState state = new GameResultState();

    public GameResultViewModel() {
        super(VIEW_NAME);
    }

    public GameResultState getState() {
        return state;
    }
    public void setState(GameResultState newState) {
        this.state = newState;
        firePropertyChange("state");
    }
}
