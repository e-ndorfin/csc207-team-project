package com.sketchandguess.interface_adapters.game;

import com.sketchandguess.interface_adapters.ViewModel;

public class GameResultViewModel extends ViewModel<GameResultState> {
    public static final String VIEW_NAME = "GameResult";

    private GameResultState state = new GameResultState();

    public GameResultViewModel() {
        super(VIEW_NAME);
        super.setState(state);
    }

    @Override
    public GameResultState getState() {
        return state;
    }

    @Override
    public void setState(GameResultState newState) {
        this.state = newState;
        super.setState(newState);
        firePropertyChange("state");
    }
}