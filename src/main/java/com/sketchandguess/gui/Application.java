package com.sketchandguess.gui;

import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.database.UserSettingsDataBase;
import com.sketchandguess.interface_adapters.ViewManagerModel;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowController;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowPresenter;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowViewModel;
import com.sketchandguess.interface_adapters.game.GameController;
import com.sketchandguess.interface_adapters.game.GamePresenter;
import com.sketchandguess.usecases.RecordGameUseCase.RecordGameUseCase;
import com.sketchandguess.usecases.select_game.SelectGameRecordUseCase;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Application extends JFrame {
    private MainMenu mainMenu;
    private Game game;
    private Gallery gallery;
    private Settings settings;
    private final GalleryWindowViewModel galleryWindowViewModel;
    private final GalleryWindowController galleryWindowController;
    private final ViewManagerModel viewManagerModel;
    private GameResult gameResult;
    private UserSettingsDataBase userSettingsDataBase;
    private GameDataBase gameDataBase;
    
    public Application() {
        setTitle("Sketch and Guess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Initialize ViewManagerModel
        viewManagerModel = new ViewManagerModel(); // Instantiate ViewManagerModel

        // Initialize GalleryWindow components
        galleryWindowViewModel = new GalleryWindowViewModel();
        GalleryWindowPresenter galleryWindowPresenter = new GalleryWindowPresenter(galleryWindowViewModel);
        SelectGameRecordUseCase selectGameRecordUseCase = new SelectGameRecordUseCase(galleryWindowPresenter);
        galleryWindowController = new GalleryWindowController(selectGameRecordUseCase, viewManagerModel); // Pass viewManagerModel
        
        userSettingsDataBase = new UserSettingsDataBase();
        gameDataBase = new GameDataBase(); // Initialize GameDataBase

        // Initialize Game/Record Components
        GamePresenter gamePresenter = new GamePresenter(viewManagerModel);
        RecordGameUseCase recordGameUseCase = new RecordGameUseCase(gameDataBase, gamePresenter);
        GameController gameController = new GameController(recordGameUseCase);

        // Initialize views
        mainMenu = new MainMenu(this);
        game = new Game(this, gameController);
        gameResult = new GameResult(this);
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

        // Add PropertyChangeListener to ViewManagerModel to switch view when state changes
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
                        case "GameResult":
                            showGameResult();
                            break;
                    }
                }
            }
        });

        // Starting point is main menu
        showMainmenu();

        setVisible(true);
    }

    public void startNewGame() {
        game.resetCompletely();
        showGame();
    }

    public void retryGame() {
        game.resetForRetry();
        showGame();
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

    public void showGameResult() {
        setContentPane(gameResult);
        revalidate();
        repaint();
    }

    public Game getGamePanel() {
        return game;
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
