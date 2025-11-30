package com.sketchandguess.interface_adapters.gallery;

import com.sketchandguess.gui.Gallery;
import com.sketchandguess.interface_adapters.ViewModel;

public class GalleryViewModel extends ViewModel<GalleryState> {
    private static final String VIEW_NAME = "Gallery";
    private GalleryState state = new GalleryState();

    public GalleryViewModel() {
        super(VIEW_NAME);
        super.setState(this.state);
    }

    public GalleryState getState() {
        return state;
    }

    @Override
    public void setState(GalleryState state) {
        this.state = state;
        super.setState(state);
        firePropertyChange("state");
    }
}
