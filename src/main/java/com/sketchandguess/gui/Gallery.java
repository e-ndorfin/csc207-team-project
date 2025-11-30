package com.sketchandguess.gui;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

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
    private final JButton backButton = new JButton("Back to Main Menu");
    private final JPanel galleryGridPanel;
    private final JScrollPane galleryScrollPane;
    private final JPanel centerPanel = new JPanel(new CardLayout());
    private final JLabel emptyLabel = new JLabel(emptyGallery, SwingConstants.CENTER);

    private final GalleryWindowController galleryWindowController;
    private final Application app;


    public Gallery(Application app, GalleryWindowController galleryWindowController) {
        this.app = app;
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
        
        // Configure scroll speed for faster scrolling
        galleryScrollPane.getVerticalScrollBar().setUnitIncrement(16); // increase for faster scrolling
        
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
            app.showMainmenu();
        });
    }
    
    /**
     * Refreshes the gallery by reloading the database from the CSV file
     * and updating the view - called in `Application.java` when gallery is opened.
     */
    public void refresh() {
        // Reload the database from CSV
        this.mainDataBase = new GameDataBase();
        // Reset currentDataBase to mainDataBase (in case we were in a search)
        this.currentDataBase = mainDataBase;
        // Update the view with the fresh data
        updateGalleryView();
    }
    
    private void updateGalleryView() {
        galleryGridPanel.removeAll();
        CardLayout cl = (CardLayout)(centerPanel.getLayout());

        if (currentDataBase.GameData.isEmpty()) {
            cl.show(centerPanel, "empty");
        } else {
            // Reverse the list to show newest (bottom of CSV) at the top
            var reversedRecords = new java.util.ArrayList<>(currentDataBase.GameData);
            Collections.reverse(reversedRecords);
            
            for (GameRecord record : reversedRecords) {
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
}
