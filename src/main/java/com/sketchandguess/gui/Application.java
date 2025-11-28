package com.sketchandguess.gui;

import com.sketchandguess.api.APIHandler;
import com.sketchandguess.api.HuggingFaceAPICaller;
import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.database.UserSettingsDataBase;
import com.sketchandguess.interface_adapters.ViewManagerModel;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowController;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowPresenter;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowViewModel;
import com.sketchandguess.interface_adapters.game.GameController;
import com.sketchandguess.interface_adapters.game.GamePresenter;
import com.sketchandguess.interface_adapters.game.GameResultViewModel;
import com.sketchandguess.interface_adapters.game.GameViewModel;
import com.sketchandguess.usecases.RecordGameUseCase.RecordGameUseCase;
import com.sketchandguess.usecases.gameplay.GameplayUseCase;
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
        viewManagerModel = new ViewManagerModel(); 

        // Initialize GalleryWindow components
        galleryWindowViewModel = new GalleryWindowViewModel();
        GalleryWindowPresenter galleryWindowPresenter = new GalleryWindowPresenter(galleryWindowViewModel);
        SelectGameRecordUseCase selectGameRecordUseCase = new SelectGameRecordUseCase(galleryWindowPresenter);
        galleryWindowController = new GalleryWindowController(selectGameRecordUseCase, viewManagerModel);
        
        userSettingsDataBase = UserSettingsDataBase.getInstance();
        gameDataBase = new GameDataBase(); 

        // Initialize API components
        APIHandler apiHandler = new APIHandler("https://zachttang-quickdraw.hf.space/predict");
        HuggingFaceAPICaller apiCaller = new HuggingFaceAPICaller(apiHandler);

        // Initialize Game ViewModels
        GameViewModel gameViewModel = new GameViewModel();
        GameResultViewModel gameResultViewModel = new GameResultViewModel();

        // Initialize Game/Record Components
        GamePresenter gamePresenter = new GamePresenter(viewManagerModel, gameResultViewModel, gameViewModel);
        
        RecordGameUseCase recordGameUseCase = new RecordGameUseCase(gameDataBase, gamePresenter);
        GameplayUseCase gameplayUseCase = new GameplayUseCase(apiCaller, gamePresenter);
        
        GameController gameController = new GameController(recordGameUseCase, gameplayUseCase);

        // Initialize views
        mainMenu = new MainMenu(this);
        game = new Game(gameController, gameViewModel);
        gameResult = new GameResult(this);
        gallery = new Gallery(galleryWindowController); 
        settings = new Settings(this, userSettingsDataBase);

        // Add PropertyChangeListener to GalleryWindowViewModel
        galleryWindowViewModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("state")) {
                    if (galleryWindowViewModel.getState().getCurrentRecord() != null) {
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
        //
        // Called when new game is started from main menu / retry game
        //
        
        // Initialize default difficulty
        String diffName = "Medium";
        double timeLimit = 30.0;

        // Check if user settings are available
        if (userSettingsDataBase != null && userSettingsDataBase.getUserSettings() != null) {
            com.sketchandguess.entities.UserSettings settings = userSettingsDataBase.getUserSettings();
            String difficultyNameFromSettings = settings.getDifficultyName();
            
            // Capitalize first letter of difficulty name to match Difficulty class expectations
            if (difficultyNameFromSettings != null && !difficultyNameFromSettings.isEmpty()) {
                diffName = difficultyNameFromSettings.substring(0, 1).toUpperCase() + 
                          difficultyNameFromSettings.substring(1).toLowerCase();
            }
            // Use the custom time limit from settings
            timeLimit = settings.getDefaultTimeLimit();
        }
        
        // Create Difficulty object with the difficulty name
        com.sketchandguess.entities.Difficulty difficulty = new com.sketchandguess.entities.Difficulty(diffName);
        
        game.resetForNewGame();  // Clears canvas, clears predictions, resets tool
        game.setDifficultyText(difficulty.getDifficultyName());  // Sets difficulty text in game view
        game.setPromptText(difficulty.getRandomPrompt());  // Sets prompt text in game view
        
        // Sets the time limit from user settings
        game.setTimeLimitSeconds(timeLimit);
        
        // System.out.println("[DEBUG] Starting countdown in Application...");
        game.startCountdown();
        
        showGame();  // Shows the game view
    }

    public void retryGame() {
        game.resetForRetry();  // Clears canvas, resets tool, sets time limit, starts countdown
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
        gallery.refresh();
        setContentPane(gallery);
        revalidate();
        repaint();
    }

    public void showSettings() {
        settings.refresh();
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