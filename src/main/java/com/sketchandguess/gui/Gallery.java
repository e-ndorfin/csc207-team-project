package com.sketchandguess.gui;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.database.SearchObject;
import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.interface_adapters.gallery.GalleryDatabaseInteractor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

// TODO ensure this does not call usecase methods, if it does, use a controller
public class Gallery extends JPanel {
    // this database represents the "main" database of images we are drawing from; it will be the database shown by default
    public GalleryDatabaseInteractor mainDataBase;
    // this database represents the current database being shown. Usually, this is the MainDataBase, but it will change when the search bar is used.
    private final JTextField searchBarField = new JTextField(15);
    private final JPanel galleryGridPanel;
    private final JPanel centerPanel = new JPanel(new CardLayout());


    public Gallery() {
        this.setLayout(new BorderLayout());
        this.mainDataBase = new GalleryDatabaseInteractor();
        JPanel searchBar = new JPanel();
        searchBar.add(searchBarField);
        JButton searchButton = new JButton("Search");
        searchBar.add(searchButton);
        JButton clearButton = new JButton("Clear");
        searchBar.add(clearButton);
        add(searchBar, BorderLayout.NORTH);

        galleryGridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        JScrollPane galleryScrollPane = new JScrollPane(galleryGridPanel);

        centerPanel.add(galleryScrollPane, "gallery");
        String emptyGallery = "No Pictures Found";
        JLabel emptyLabel = new JLabel(emptyGallery, SwingConstants.CENTER);
        centerPanel.add(emptyLabel, "empty");
        add(centerPanel, BorderLayout.CENTER);

        updateGalleryView();

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchObject searchQuery = new SearchObject(searchBarField.getText());
                mainDataBase.SearchDB(searchQuery);
                updateGalleryView();
            }
                                       });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainDataBase = new GalleryDatabaseInteractor();
                searchBarField.setText("");
                updateGalleryView();
            }
        });
    }
    private void updateGalleryView() {
        galleryGridPanel.removeAll();
        CardLayout cl = (CardLayout)(centerPanel.getLayout());

        if (mainDataBase.isEmpty()) {
            cl.show(centerPanel, "empty");
        } else {
            var ReversedRecords = mainDataBase.getGameData();
            Collections.reverse(ReversedRecords);

            for (GameRecord record : ReversedRecords) {
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
                    // TODO: Slot in GalleryWindow.java logic here.
                    // For example: new GalleryWindow(record);
                    System.out.println("Clicked image for prompt: " + record.getPrompt());
                });

                galleryGridPanel.add(imageButton);
            }
            cl.show(centerPanel, "gallery");
        }

        revalidate();
        repaint();
    }
}
