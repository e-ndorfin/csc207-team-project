package com.sketchandguess.gui;

import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowController;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowPresenter;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowViewModel;
import com.sketchandguess.usecases.select_game.SelectGameRecordUseCase;
import com.sketchandguess.database.UserSettingsDataBase;
import com.sketchandguess.interface_adapters.ViewManagerModel; // Import ViewManagerModel

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

interface RecordGameController {
    void onDoneButtonClicked(java.awt.image.BufferedImage image);
}

public class Application extends JFrame {
    private MainMenu mainMenu;
    private Game game;
    private Gallery gallery;
    private Settings settings;

    private final GalleryWindowViewModel galleryWindowViewModel;
    private final GalleryWindowController galleryWindowController;
    private final ViewManagerModel viewManagerModel; // Declare ViewManagerModel

    private UserSettingsDataBase userSettingsDataBase;
    
    public Application() {
        setTitle("Sketch and Guess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Mock controller
        RecordGameController mockController = new RecordGameController() {
            @Override
            public void onDoneButtonClicked(java.awt.image.BufferedImage image) {
                System.out.println("Game completed! Image saved.");
                showMainmenu();
            }
        };

        // Initialize ViewManagerModel
        viewManagerModel = new ViewManagerModel(); // Instantiate ViewManagerModel

        // Initialize GalleryWindow components
        galleryWindowViewModel = new GalleryWindowViewModel();
        GalleryWindowPresenter galleryWindowPresenter = new GalleryWindowPresenter(galleryWindowViewModel);
        SelectGameRecordUseCase selectGameRecordUseCase = new SelectGameRecordUseCase(galleryWindowPresenter);
        galleryWindowController = new GalleryWindowController(selectGameRecordUseCase, viewManagerModel); // Pass viewManagerModel
        
        userSettingsDataBase = new UserSettingsDataBase();

        // Initialize views
        mainMenu = new MainMenu(this);
        game = new Game(this, mockController);
        gallery = new Gallery(galleryWindowController); // Pass the controller to Gallery
        settings = new Settings(this, userSettingsDataBase);

        // Add PropertyChangeListener to GalleryWindowViewModel
        galleryWindowViewModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("state")) {
                    if (galleryWindowViewModel.getState().getCurrentRecord() != null) {
                        // Create and show PictureWindow when a record is selected
                        PictureWindow pictureWindow = new PictureWindow(galleryWindowViewModel, galleryWindowController);
                        pictureWindow.setVisible(true);
                    }
                }
            }
        });

        // Add PropertyChangeListener to ViewManagerModel for view switching
        viewManagerModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("view")) {
                    String activeView = (String) evt.getNewValue();
                    switch (activeView) {
                        case "MainMenu":
                            showMainmenu();
                            break;
                        case "Game":
                            showGame();
                            break;
                        case "Gallery":
                            showGallery();
                            break;
                        case "Settings":
                            showSettings();
                            break;
                        // Add other cases as needed
                    }
                }
            }
        });
        
        // Starting point is main menu
        showMainmenu();

        setVisible(true);
    }

    public void showMainmenu() {
        setContentPane(mainMenu);
        revalidate();
        repaint();
    }

    public void showGame() {
        setContentPane(game);
        revalidate();
        repaint();
    }

    public void showGallery() {
        setContentPane(gallery);
        revalidate();
        repaint();
    }

    public void showSettings() {
        setContentPane(settings);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Application();
            }
        });
    }
}
