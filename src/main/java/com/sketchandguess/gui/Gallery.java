package com.sketchandguess.gui;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO ensure this does not call usecase methods, if it does, use a controller
public class Gallery extends JPanel {
    private final String viewName = "Drawing Gallery";
    private final String emptyGallery = "No Pictures Found";
    // this database represents the "main" database of images we are drawing from; it will be the database shown by default
    public GameDataBase mainDataBase;
    // this database represents the current database being shown. Usually, this is the MainDataBase, but it will change when the search bar is used.
    public GameDataBase currentDataBase;

    private final JTextField searchBarField = new JTextField(15);
    private final JPanel searchBar = new JPanel();
    private final JButton searchButton = new JButton("Search");
    private final JButton clearButton = new JButton("Clear");
    private final JButton backButton = new JButton("Back to Main Menu"); // New back button
    private final JPanel galleryGridPanel;
    private final JScrollPane galleryScrollPane;
    private final JPanel centerPanel = new JPanel(new CardLayout());
    private final JLabel emptyLabel = new JLabel(emptyGallery, SwingConstants.CENTER);

    private final GalleryWindowController galleryWindowController;


    public Gallery(GalleryWindowController galleryWindowController) {
        this.galleryWindowController = galleryWindowController;
        this.setLayout(new BorderLayout());
        this.mainDataBase = new GameDataBase();
        this.currentDataBase = mainDataBase;

        JPanel topControlsPanel = new JPanel(new BorderLayout()); // Panel to hold back button and search bar

        JPanel searchPanel = new JPanel(); // Original search bar panel
        searchPanel.add(searchBarField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        topControlsPanel.add(backButton, BorderLayout.WEST); // Add back button to the left
        topControlsPanel.add(searchPanel, BorderLayout.CENTER); // Add search bar to the center

        add(topControlsPanel, BorderLayout.NORTH); // Add the combined top panel to the frame

        galleryGridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        galleryScrollPane = new JScrollPane(galleryGridPanel);

        centerPanel.add(galleryScrollPane, "gallery");
        centerPanel.add(emptyLabel, "empty");
        add(centerPanel, BorderLayout.CENTER);

        updateGalleryView();

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentDataBase = mainDataBase.SearchWord(searchBarField.getText());
                updateGalleryView();
            }
                                       });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentDataBase = mainDataBase;
                updateGalleryView();
            }
        });

        backButton.addActionListener(e -> {
            galleryWindowController.goBackToMainMenu();
        });
    }
    private void updateGalleryView() {
        galleryGridPanel.removeAll();
        CardLayout cl = (CardLayout)(centerPanel.getLayout());

        if (currentDataBase.GameData.isEmpty()) {
            cl.show(centerPanel, "empty");
        } else {
            for (GameRecord record : currentDataBase.GameData) {
                ImageIcon icon = new ImageIcon(record.getImagePath());
                Image image = icon.getImage();
                // Scale image to a thumbnail size
                Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                JButton imageButton = new JButton(scaledIcon);
                imageButton.setText(record.getPrompt());
                imageButton.setVerticalTextPosition(SwingConstants.BOTTOM);
                imageButton.setHorizontalTextPosition(SwingConstants.CENTER);
                imageButton.setToolTipText(record.getPrompt());
                imageButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                imageButton.setContentAreaFilled(false);

                // Add an action listener to handle clicks
                imageButton.addActionListener(e -> {
                    galleryWindowController.selectGameRecord(record);
                });

                galleryGridPanel.add(imageButton);
            }
            cl.show(centerPanel, "gallery");
        }

        revalidate();
        repaint();
    }

    public void refresh() {
        this.mainDataBase = new  GameDataBase();
        this.currentDataBase = mainDataBase;
        updateGalleryView();
    }
}
