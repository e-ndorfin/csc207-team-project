package com.sketchandguess.gui;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.FlowLayout;

import com.sketchandguess.database.UserSettingsDataBase;

public class MainMenu extends JPanel {

    private final Application app;
    private final UserSettingsDataBase userSettingsDataBase;

    public MainMenu(Application app, UserSettingsDataBase userSettingsDataBase) {
        this.app = app;
        this.userSettingsDataBase = userSettingsDataBase;

        setLayout(new FlowLayout());

        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> app.showSettings());

        add(settingsButton);
    }
}
