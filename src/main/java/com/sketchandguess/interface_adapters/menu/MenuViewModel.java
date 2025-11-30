package com.sketchandguess.interface_adapters.menu;

import com.sketchandguess.interface_adapters.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MenuViewModel extends ViewModel {
    public static final String VIEW_NAME = "MainMenu";
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public MenuViewModel() {
        super(VIEW_NAME);
    }

    @Override
    public void firePropertyChange() {
        support.firePropertyChange("state", null, null);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
