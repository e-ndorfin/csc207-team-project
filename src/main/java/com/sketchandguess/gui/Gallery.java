package com.sketchandguess.gui;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.database.SearchObject;
import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.interface_adapters.gallery.GalleryController;
import com.sketchandguess.interface_adapters.gallery.GalleryDatabaseInteractor;
import com.sketchandguess.interface_adapters.gallery.GalleryViewModel;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;

public class Gallery extends JPanel implements PropertyChangeListener {
    private final GalleryController controller;
    private final GalleryViewModel viewModel;

    // this database represents the "main" database of images we are drawing from; it will be the database shown by default
    // this database represents the current database being shown. Usually, this is the MainDataBase, but it will change when the search bar is used.
    private final JTextField searchBarField = new JTextField(15);
    private final JPanel galleryGridPanel;
    private final JPanel centerPanel = new JPanel(new CardLayout());
    private final JButton backButton = new JButton("Back to Main Menu");
    private final GalleryWindowController galleryWindowController;
    private final Application app;



    public Gallery(Application app, GalleryWindowController windowController, GalleryController controller, GalleryViewModel viewModel) {
        this.app = app;
        this.galleryWindowController = windowController;
        this.controller = controller;
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);
        this.setLayout(new BorderLayout());
        JPanel searchBar = new JPanel();
        searchBar.add(searchBarField);
        JButton searchButton = new JButton("Search");
        searchBar.add(searchButton);
        JButton clearButton = new JButton("Clear");
        searchBar.add(clearButton);


        JPanel topControlsPanel = new JPanel(new BorderLayout()); // Panel to hold back button and search bar
        topControlsPanel.add(searchBar, BorderLayout.CENTER);
        topControlsPanel.add(backButton, BorderLayout.WEST);
        add(topControlsPanel, BorderLayout.NORTH);

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
                controller.searchGames(searchBarField.getText());
                updateGalleryView();
            }
                                       });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clearSearch();
                updateGalleryView();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.returnToMainMenu();
            }
        });
    }
    public void refresh() {
        controller.refreshGallery();
        updateGalleryView();
    }

    private void updateGalleryView() {
        galleryGridPanel.removeAll();
        CardLayout cl = (CardLayout)(centerPanel.getLayout());

        if (viewModel.getState().isEmpty()) {
            cl.show(centerPanel, "empty");
        } else {
            var ReversedRecords = viewModel.getState().getGameRecords();
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
                    galleryWindowController.setRecord(record);
                    controller.selectGameRecord(record);
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

    public void propertyChange(PropertyChangeEvent evt) {
        updateGalleryView();
    }
}
