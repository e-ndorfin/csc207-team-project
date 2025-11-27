package com.sketchandguess.interface_adapters.game;

import com.sketchandguess.interface_adapters.ViewModel;

import java.beans.PropertyChangeListener;

public class GameViewModel extends ViewModel {

    public static final String VIEW_NAME = "Game";

    private GameState state = new GameState();

    public GameViewModel() {
        super("Game");
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState newstate) {
        this.state = newstate;
        firePropertyChange("state");
    }
}