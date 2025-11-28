package com.sketchandguess.gui;

import com.sketchandguess.interface_adapters.game.GameResultState;
import com.sketchandguess.interface_adapters.game.GameResultViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class GameResult extends JPanel implements PropertyChangeListener {

    private final Application app;   // to switch screens
    private final GameResultViewModel viewModel;
    private final JLabel titleLabel;
    private final JLabel promptLabel;
    private final JLabel aiGuessLabel;
    private final JLabel timeTakenLabel;
    private final JLabel imageLabel;

    public GameResult(Application app, GameResultViewModel viewModel) {
        this.app = app;
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            GameResultState state = (GameResultState) evt.getNewValue();
            updateUI(state);
        }
    }

    private void updateUI(GameResultState state) {
        titleLabel.setText(state.getEndingMessage());
        promptLabel.setText("Prompt: " + state.getPrompt());
        aiGuessLabel.setText("AI Guess: " + state.getAiGuess());
        timeTakenLabel.setText(String.format("Time Taken: %.1f / %.1f s", state.getTimeTaken(), state.getTimeLimit()));

        // Update Image
        String imagePath = state.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                // Scale to fit around half the screen width/height
                Image img = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
                imageLabel.setText(""); // Clear text if image loaded
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("Image not found");
            }
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("No Image");
        }
    }
}
