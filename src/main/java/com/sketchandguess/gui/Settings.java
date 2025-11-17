package com.sketchandguess.gui;

import com.sketchandguess.database.UserSettingsDataBase;
import com.sketchandguess.entities.UserSettings;

import javax.swing.*;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;


/**
 * Settings screen: lets the user change the default game time limit.
 */
public class Settings extends JPanel {

    private static final int MIN_TIME_LIMIT = 30;
    private static final int MAX_TIME_LIMIT = 300;

    private final Application app;
    private final UserSettingsDataBase userSettingsDataBase;

    private final JTextField timeLimitField;
    private final JLabel messageLabel;

    public Settings(Application app, UserSettingsDataBase userSettingsDataBase) {
        this.app = app;
        this.userSettingsDataBase = userSettingsDataBase;

        setLayout(new BorderLayout(10, 10));

        // ---------- Title ----------
        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        add(titleLabel, BorderLayout.NORTH);

        // ---------- Center panel: time limit field ----------
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        JLabel timeLimitLabel = new JLabel("Default Time Limit (seconds):");
        timeLimitField = new JTextField(10);

        centerPanel.add(timeLimitLabel);
        centerPanel.add(timeLimitField);

        // empty cells to keep layout simple
        centerPanel.add(new JLabel(""));
        centerPanel.add(new JLabel(""));

        add(centerPanel, BorderLayout.CENTER);

        // ---------- Bottom panel: buttons + message ----------
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

        // Load current settings into the text field
        loadCurrentSettings();

        // ---------- Listeners ----------
        saveButton.addActionListener(e -> onSaveClicked());
        backButton.addActionListener(e -> app.showMainmenu());
    }

    /**
     * Reads the current settings from the "database"
     * and shows the default time limit in the text field.
     */
    private void loadCurrentSettings() {
        UserSettings settings = userSettingsDataBase.getUserSettings();
        int timeLimit = (int) Math.round(settings.getDefaultTimeLimit());
        timeLimitField.setText(String.valueOf(timeLimit));
    }

    /**
     * Handles clicking the Save button.
     * Validates the input and either saves or shows an error.
     */
    private void onSaveClicked() {
        String text = timeLimitField.getText().trim();

        try {
            int requested = Integer.parseInt(text);

            if (requested < MIN_TIME_LIMIT || requested > MAX_TIME_LIMIT) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText(
                        "Time limit must be between " + MIN_TIME_LIMIT + " and " + MAX_TIME_LIMIT + " seconds."
                );
                // revert to previous valid value
                loadCurrentSettings();
                return;
            }

            // Valid value: save to our "database"
            UserSettings settings = userSettingsDataBase.getUserSettings();
            settings.setDefaultTimeLimit(requested);
            userSettingsDataBase.saveUserSettings(settings);

            messageLabel.setForeground(new Color(0, 128, 0)); // dark green
            messageLabel.setText("Time limit updated successfully.");

        } catch (NumberFormatException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Please enter a whole number of seconds.");
        }
    }
}
