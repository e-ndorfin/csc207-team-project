package com.sketchandguess.gui;

import javax.swing.*;
import java.awt.*;

interface RecordGameController {
    void onDoneButtonClicked(java.awt.image.BufferedImage image);
}

public class Application extends JFrame {
    private MainMenu mainMenu;
    private Game game;
    private Gallery gallery;
    private Settings settings;
    private GameResult gameResult;
    
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
                showGameResult();
            }
        };
        
        // Initialize views
        mainMenu = new MainMenu(this);
        game = new Game(this, mockController);
        gallery = new Gallery();
        settings = new Settings();
        gameResult = new GameResult(this);
        
        // Starting point is main menu
        showMainmenu();
        
        setVisible(true);
    }

    public void startNewGame() {
        game.resetCompletely();
//        recordGame.controller.startNewGame();
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
