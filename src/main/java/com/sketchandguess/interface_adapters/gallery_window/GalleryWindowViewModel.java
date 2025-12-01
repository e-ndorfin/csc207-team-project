package com.sketchandguess.interface_adapters.gallery_window;

import com.sketchandguess.interface_adapters.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GalleryWindowViewModel extends ViewModel {

    public static final String VIEW_NAME = "gallery_window";
    private GalleryWindowState state = new GalleryWindowState();

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public GalleryWindowViewModel() {
        super(VIEW_NAME);
    }

    public void setState(GalleryWindowState state) {
        this.state = state;
    }

    public GalleryWindowState getState() {
        return state;
    }

    @Override
    public void firePropertyChange() {
        support.firePropertyChange("state", null, this.state);
    }

    public void fireDeletionEvent() {
        support.firePropertyChange("deleted", null, true);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
