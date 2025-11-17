package com.sketchandguess.gui;

import com.sketchandguess.database.UserSettingsDataBase;

import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;

import com.sketchandguess.database.UserSettingsDataBase;


public class Application extends JFrame {

    private final UserSettingsDataBase userSettingsDataBase;

    private final JPanel rootPanel;
    private final CardLayout cardLayout;

    private final MainMenu mainMenu;
    private final Settings settings;
    // later you can add: private final Game game;

    public Application() {
        // shared "database" instance for the whole app
        this.userSettingsDataBase = new UserSettingsDataBase();

        this.cardLayout = new CardLayout();
        this.rootPanel = new JPanel(cardLayout);

        // create screens
        this.mainMenu = new MainMenu(this, userSettingsDataBase);
        this.settings = new Settings(this, userSettingsDataBase);

        // register screens with card layout
        rootPanel.add(mainMenu, "MAIN_MENU");
        rootPanel.add(settings, "SETTINGS");

        // add to frame
        setContentPane(rootPanel);

        setTitle("Sketch and Guess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        showMainmenu();  // start at main menu
    }

    public void showMainmenu() {
        cardLayout.show(rootPanel, "MAIN_MENU");
    }

    public void showSettings() {
        cardLayout.show(rootPanel, "SETTINGS");
    }

    // later you can add something like:
    // public void showGame(RecordGameController controller) { ... }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Application app = new Application();
            app.setVisible(true);
        });
    }
}
