package com.sketchandguess.gui;

import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowController;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowState;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowViewModel;
import com.sketchandguess.entities.GameRecord;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PictureWindow extends JFrame implements PropertyChangeListener {

    private final GalleryWindowViewModel GalleryWindowViewModel;
    private final GalleryWindowController GalleryWindowController;

    private JLabel imageLabel;
    private JLabel statsHeaderLabel;
    private JLabel dateLabel;
    private JLabel promptLabel;
    private JLabel outcomeLabel;
    private JLabel errorLabel;
    private JButton saveButton;
    private JButton deleteButton;

    public PictureWindow(GalleryWindowViewModel GalleryWindowViewModel,
                         GalleryWindowController GalleryWindowController) {

        this.GalleryWindowViewModel = GalleryWindowViewModel;
        this.GalleryWindowController = GalleryWindowController;
        this.GalleryWindowViewModel.addPropertyChangeListener(this);

        setTitle("Picture");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        layoutComponents();
        updateFromState();
    }

    private void initComponents() {
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        statsHeaderLabel = new JLabel("Game details");
        statsHeaderLabel.setFont(statsHeaderLabel.getFont().deriveFont(Font.BOLD, 20f));

        dateLabel = createStatLabel("date: —");
        promptLabel = createStatLabel("prompt: —");
        outcomeLabel = createStatLabel("outcome: —");

        saveButton = new JButton("Save to Computer");
        saveButton.addActionListener(e -> {
            GalleryWindowState state = GalleryWindowViewModel.getState();
            GameRecord record = state.getCurrentRecord();

            if (record != null) {
                GalleryWindowController.saveImage();
            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            GalleryWindowState state = GalleryWindowViewModel.getState();
            GameRecord record = state.getCurrentRecord();

            if (record != null) {
                GalleryWindowController.deleteGame();
                this.dispose();
            }
        });
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 16f));
        return label;
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(16, 16));

        JScrollPane imageScroll = new JScrollPane(imageLabel);
        imageScroll.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 8));

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(32, 8, 32, 24));
        statsPanel.add(statsHeaderLabel);
        statsPanel.add(Box.createVerticalStrut(24));
        statsPanel.add(dateLabel);
        statsPanel.add(Box.createVerticalStrut(12));
        statsPanel.add(promptLabel);
        statsPanel.add(Box.createVerticalStrut(12));
        statsPanel.add(outcomeLabel);
        statsPanel.add(Box.createVerticalGlue());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, imageScroll, statsPanel);
        splitPane.setResizeWeight(0.7);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setDividerSize(6);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 12));
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 16, 0, 16));
        topPanel.add(errorLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateFromState() {
        GalleryWindowState state = GalleryWindowViewModel.getState();
        GameRecord record = state.getCurrentRecord();

        if (record != null) {
            loadImage(record.getImagePath());
            statsHeaderLabel.setText("Selected game");
            dateLabel.setText("date: " + defaultText(state.getDateText()));
            promptLabel.setText("prompt: " + defaultText(state.getPromptText()));
            outcomeLabel.setText("outcome: " + defaultText(state.getOutcomeText()));
        } else {
            clearImage();
            statsHeaderLabel.setText("Game details");
            dateLabel.setText("date: —");
            promptLabel.setText("prompt: —");
            outcomeLabel.setText("outcome: —");
        }

        saveButton.setEnabled(record != null);
        deleteButton.setEnabled(record != null);
        errorLabel.setText(state.getErrorMessage());
    }

    private void loadImage(String imagePath) {
        if (imagePath != null) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaled = icon.getImage().getScaledInstance(
                    600, -1, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
            imageLabel.setText(null);
        } else {
            clearImage();
        }
    }

    private void clearImage() {
        imageLabel.setIcon(null);
        imageLabel.setText("No picture selected");
    }

    private String defaultText(String value) {
        return (value == null || value.isEmpty()) ? "—" : value;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateFromState();
    }
}
