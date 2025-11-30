package com.sketchandguess.interface_adapters;

/**
 * Model for the View Manager. Its state is the name of the View which
 * is currently active. An initial state of "" is used.
 */
public class ViewManagerModel extends ViewModel<String> {

    private String previousState = "";

    public ViewManagerModel() {
        super("view manager");
        this.setState("");
    }

    @Override
    public void setState(String state) {
        this.previousState = this.getState();
        super.setState(state);
    }

    /**
     * Fires a property change event for the "view" property with the previous state as the old value.
     * This allows listeners to determine which view was active before the change.
     */
    @Override
    public void firePropertyChange(String propertyName) {
        if ("view".equals(propertyName)) {
            // Use the protected method to fire with old and new values
            firePropertyChange(propertyName, previousState, this.getState());
            // Update previous state after firing
            this.previousState = this.getState();
        } else {
            super.firePropertyChange(propertyName);
        }
    }

}