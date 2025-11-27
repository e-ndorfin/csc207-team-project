package com.sketchandguess.gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.JLabel;

public class GameResult extends JPanel {

    private final Application app;   // to switch screens
    private final JLabel titleLabel;
    private final JLabel promptLabel;
    private final JLabel aiGuessLabel;
    private final JLabel timeTakenLabel;
    private final JLabel imageLabel;

    public GameResult(Application app) {
        this.app = app;
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        titleLabel = new JLabel("Game Result", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        add(titleLabel, BorderLayout.NORTH);

        //left side: image
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        centerPanel.add(imageLabel);

        //right side
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        promptLabel = new JLabel("Prompt: ");
        aiGuessLabel = new JLabel("AI Guess: ");
        timeTakenLabel = new JLabel("Time Taken: ");


        infoPanel.add(promptLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(aiGuessLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(timeTakenLabel);

        centerPanel.add(infoPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton retryButton = new JButton("Retry");
        retryButton.addActionListener(e -> {
            app.retryGame();
        });
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> app.showMainmenu());
        bottomPanel.add(retryButton);
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}

//Todo: use controller to get the data about prompt, time taken....