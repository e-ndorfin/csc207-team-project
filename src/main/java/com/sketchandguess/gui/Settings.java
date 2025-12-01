package com.sketchandguess.gui;

import com.sketchandguess.interface_adapters.settings.SettingsController;
import com.sketchandguess.interface_adapters.settings.SettingsViewModel;
import com.sketchandguess.interface_adapters.settings.SettingsState;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Settings screen: lets the user change the default game time limit
 * and difficulty (Easy / Medium / Hard).
 * Implements the Observer pattern via PropertyChangeListener.
 */
public class Settings extends JPanel implements PropertyChangeListener {

    private static final int MIN_TIME_LIMIT = 15;
    private static final int MAX_TIME_LIMIT = 45;

    private final Application app;
    private final SettingsController controller;
    private final SettingsViewModel viewModel;

    // Timer slider + numeric label
    private final JSlider timeSlider;
    private final JLabel timeValueLabel;

    // Difficulty buttons
    private final JToggleButton easyButton;
    private final JToggleButton mediumButton;
    private final JToggleButton hardButton;

    private final JLabel messageLabel;

    /**
     * Constructs the Settings panel.
     *
     * @param app       the main Application, used to navigate back
     * @param controller the controller to execute use cases
     * @param viewModel the view model to observe for state changes
     */
    public Settings(Application app, SettingsController controller, SettingsViewModel viewModel) {
        this.app = app;
        this.controller = controller;
        this.viewModel = viewModel;
        
        // Add this view as a listener to the view model
        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));

        // ---------- Title ----------
        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        add(titleLabel, BorderLayout.NORTH);

        // ---------- Center panel ----------
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 1, 10, 10));

        // ===== Time limit row =====
        JPanel timePanel = new JPanel(new BorderLayout(5, 5));

        JLabel timeLimitLabel = new JLabel("Default Time Limit (seconds):");
        timePanel.add(timeLimitLabel, BorderLayout.NORTH);

        timeSlider = new JSlider(MIN_TIME_LIMIT, MAX_TIME_LIMIT);
        timeSlider.setMajorTickSpacing(10);
        timeSlider.setMinorTickSpacing(5);
        timeSlider.setPaintTicks(true);
        timeSlider.setPaintLabels(true);

        timeValueLabel = new JLabel("", SwingConstants.CENTER);

        timeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = timeSlider.getValue();
                timeValueLabel.setText(value + " s");
            }
        });

        timePanel.add(timeSlider, BorderLayout.CENTER);
        timePanel.add(timeValueLabel, BorderLayout.SOUTH);

        // ===== Difficulty row =====
        JPanel difficultyPanel = new JPanel(new BorderLayout(5, 5));
        JLabel difficultyLabel = new JLabel("Difficulty:");
        difficultyPanel.add(difficultyLabel, BorderLayout.NORTH);

        JPanel difficultyButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        easyButton = new JToggleButton("Easy");
        mediumButton = new JToggleButton("Medium");
        hardButton = new JToggleButton("Hard");

        ButtonGroup difficultyGroup = new ButtonGroup();
        difficultyGroup.add(easyButton);
        difficultyGroup.add(mediumButton);
        difficultyGroup.add(hardButton);

        difficultyButtonsPanel.add(easyButton);
        difficultyButtonsPanel.add(mediumButton);
        difficultyButtonsPanel.add(hardButton);

        difficultyPanel.add(difficultyButtonsPanel, BorderLayout.CENTER);

        centerPanel.add(timePanel);
        centerPanel.add(difficultyPanel);

        add(centerPanel, BorderLayout.CENTER);

        // ---------- Bottom panel ----------
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back To Main Menu");

        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);

        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(messageLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Load initial values using the controller
        loadCurrentSettings();

        // Listeners
        saveButton.addActionListener(e -> onSaveClicked());
        backButton.addActionListener(e -> app.showMainmenu());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            SettingsState state = viewModel.getState();
            if (state != null) {
                updateUIFromState(state);
            }
        }
    }

    /**
     * Updates the UI components from the current SettingsState.
     */
    private void updateUIFromState(SettingsState state) {
        // Update time slider and label
        double timeLimit = state.getDefaultTimeLimit();
        int timeValue = (int) Math.round(timeLimit);
        if (timeValue < MIN_TIME_LIMIT) {
            timeValue = MIN_TIME_LIMIT;
        } else if (timeValue > MAX_TIME_LIMIT) {
            timeValue = MAX_TIME_LIMIT;
        }
        timeSlider.setValue(timeValue);
        timeValueLabel.setText(timeValue + " s");

        // Update difficulty buttons
        String difficultyName = state.getDifficultyName();
        if (difficultyName == null || difficultyName.equalsIgnoreCase("medium")) {
            mediumButton.setSelected(true);
        } else if (difficultyName.equalsIgnoreCase("easy")) {
            easyButton.setSelected(true);
        } else if (difficultyName.equalsIgnoreCase("hard")) {
            hardButton.setSelected(true);
        } else {
            mediumButton.setSelected(true);
        }

        // Always update the message label based on current state
        String successMsg = state.getSuccessMessage();
        String errorMsg = state.getErrorMessage();
        
        if (!successMsg.isEmpty()) {
            // Show success message
            messageLabel.setForeground(new Color(0, 128, 0)); // dark green
            messageLabel.setText(successMsg);
        } else if (!errorMsg.isEmpty()) {
            // Show error message
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(errorMsg);
        } else {
            // No message - clear the label
            messageLabel.setText(" ");
            messageLabel.setForeground(Color.BLACK);
        }
    }

    /**
     * Refreshes the settings panel by reloading current settings.
     * Should be called when the panel is shown.
     */
    public void refresh() {
        // Clear any existing messages before refreshing
        SettingsState state = viewModel.getState();
        if (state != null) {
            state.setSuccessMessage("");
            state.setErrorMessage("");
            viewModel.firePropertyChange("state");
        }
        loadCurrentSettings();
    }

    /**
     * Loads the current settings using the controller.
     */
    private void loadCurrentSettings() {
        controller.executeRetrieveSettings();
    }

    /**
     * Handles clicking the Save button.
     * Uses SettingsController to validate and save.
     */
    private void onSaveClicked() {
        int requested = timeSlider.getValue();

        String newDifficultyName;
        if (easyButton.isSelected()) {
            newDifficultyName = "easy";
        } else if (hardButton.isSelected()) {
            newDifficultyName = "hard";
        } else {
            newDifficultyName = "medium";
        }

        controller.executeEditSettings(requested, newDifficultyName);
    }
}
