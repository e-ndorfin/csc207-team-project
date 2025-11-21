package com.sketchandguess.gui;

import com.sketchandguess.database.UserSettingsDataBase;
import com.sketchandguess.entities.UserSettings;
import com.sketchandguess.usecases.EditSettingsUseCase;
import com.sketchandguess.usecases.RetrieveSettingsUseCase;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Settings screen: lets the user change the default game time limit
 * and difficulty (Easy / Medium / Hard).
 */
public class Settings extends JPanel {

    private static final int MIN_TIME_LIMIT = 30;
    private static final int MAX_TIME_LIMIT = 300;

    private final Application app;
    private final UserSettingsDataBase userSettingsDataBase;
    private final EditSettingsUseCase editSettingsUseCase;
    private final RetrieveSettingsUseCase retrieveSettingsUseCase;

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
     * @param app                  the main Application, used to navigate back
     * @param userSettingsDataBase the database storing user settings
     */
    public Settings(Application app, UserSettingsDataBase userSettingsDataBase) {
        this.app = app;
        this.userSettingsDataBase = userSettingsDataBase;
        this.editSettingsUseCase = new EditSettingsUseCase(userSettingsDataBase);
        this.retrieveSettingsUseCase = new RetrieveSettingsUseCase(userSettingsDataBase);

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
        timeSlider.setMajorTickSpacing(30);
        timeSlider.setMinorTickSpacing(10);
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
        JButton backButton = new JButton("Back");

        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);

        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(messageLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Load initial values using the use case
        loadCurrentSettings();

        // Listeners
        saveButton.addActionListener(e -> onSaveClicked());
        backButton.addActionListener(e -> app.showMainmenu());
    }

    /**
     * Reads the current settings using the RetrieveSettingsUseCase
     * and initializes the controls.
     */
    private void loadCurrentSettings() {
        UserSettings settings = retrieveSettingsUseCase.retrieveSettings();

        // --- Time limit ---
        int timeLimit = (int) Math.round(settings.getDefaultTimeLimit());
        if (timeLimit < MIN_TIME_LIMIT) {
            timeLimit = MIN_TIME_LIMIT;
        } else if (timeLimit > MAX_TIME_LIMIT) {
            timeLimit = MAX_TIME_LIMIT;
        }
        timeSlider.setValue(timeLimit);
        timeValueLabel.setText(timeLimit + " s");

        // --- Difficulty (stored as a String in UserSettings) ---
        String difficultyName = settings.getDifficultyName(); // may be null if not set yet

        if (difficultyName == null) {
            mediumButton.setSelected(true); // default
        } else if (difficultyName.equalsIgnoreCase("easy")) {
            easyButton.setSelected(true);
        } else if (difficultyName.equalsIgnoreCase("hard")) {
            hardButton.setSelected(true);
        } else {
            mediumButton.setSelected(true);
        }
    }

    /**
     * Handles clicking the Save button.
     * Uses EditSettingsUseCase to validate and save.
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

        try {
            editSettingsUseCase.editSettings(requested, newDifficultyName);
            messageLabel.setForeground(new Color(0, 128, 0)); // dark green
            messageLabel.setText("Settings updated successfully.");
        } catch (IllegalArgumentException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(ex.getMessage());
            // reload previous valid settings
            loadCurrentSettings();
        }
    }
}
