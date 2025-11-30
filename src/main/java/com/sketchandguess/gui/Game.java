package com.sketchandguess.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.entities.Prediction;
import com.sketchandguess.interface_adapters.game.GameController;
import com.sketchandguess.interface_adapters.game.GameState;
import com.sketchandguess.interface_adapters.game.GameViewModel;

public class Game extends JPanel implements PropertyChangeListener {
    private final GameController controller;
    private final GameViewModel viewModel;

    private String prompt; // current game's prompt
    private JLabel promptLabel;
    private JLabel difficultyLabel;
    private JLabel timerLabel;
    private JPanel predictionsPanel; // Changed from JTextArea to JPanel for progress bars
    
    private double timeLimitSeconds; 
    private double timeLeftSeconds; 
    private javax.swing.Timer countdownTimer;
    private DrawingCanvas canvas;
    private boolean hasStartedDrawing = false;

    private Cursor pencilCursor;
    private Cursor eraserCursor;
    private Cursor currentToolCursor = null;

    private enum Tool { NONE, PEN, ERASER }
    private Tool currentTool = Tool.PEN;
    private boolean isGameFinishing = false; // Guard flag to prevent duplicate game finishes
    private boolean hasFinishedGame = false; // Track if finishGame has already been executed

    public Game(GameController controller, GameViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        canvas = new DrawingCanvas();
        add(canvas, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        
        JPanel topright = new JPanel(new GridLayout(3, 1)); 
        promptLabel = new JLabel("Prompt: "); 
        timerLabel = new JLabel("Time left: "); 
        difficultyLabel = new JLabel("Difficulty: "); 
        topright.add(promptLabel);
        topright.add(timerLabel);
        topright.add(difficultyLabel);
        rightPanel.add(topright, BorderLayout.NORTH);

        // Center Right: Predictions
        JPanel centerRight = new JPanel(new BorderLayout());
        centerRight.setBorder(BorderFactory.createTitledBorder("AI Guesses"));
        predictionsPanel = new JPanel();
        predictionsPanel.setLayout(new BoxLayout(predictionsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(predictionsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        centerRight.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(centerRight, BorderLayout.CENTER);

        // Bottom Right: Controls
        JPanel bottomright = new JPanel(new GridLayout(4, 1, 10, 10)); 
        JButton giveUpButton = new JButton("Give Up");
        JButton clearButton = new JButton("Clear");
        JButton penButton = new JButton("Pen");
        JButton eraserButton = new JButton("Eraser");

        bottomright.add(clearButton);
        bottomright.add(giveUpButton);
        bottomright.add(eraserButton);
        bottomright.add(penButton);
        rightPanel.add(bottomright, BorderLayout.SOUTH);

        rightPanel.setPreferredSize(new Dimension(250, 0));
        add(rightPanel, BorderLayout.EAST);

        giveUpButton.addActionListener(e -> {
            finishGame(false); // User gave up
        });

        clearButton.addActionListener(e -> {
            canvas.clearCanvas();
            resetTool();
            hasStartedDrawing = false;
        });

        penButton.addActionListener(e -> {
            currentTool = Tool.PEN;
            currentToolCursor = getPencilCursor();
            canvas.setCursor(currentToolCursor);
        });

        eraserButton.addActionListener(e -> {
            currentTool = Tool.ERASER;
            currentToolCursor = getEraserCursor();
            canvas.setCursor(currentToolCursor);
        });
        resetTool();
    }

    private void finishGame(boolean forceWin) {
        // Guard: prevent duplicate execution
        if (hasFinishedGame) {
            System.out.println("[DEBUG] finishGame: Game has already finished, ignoring duplicate call");
            return;
        }
        
        // Mark that we're finishing/executing finishGame
        hasFinishedGame = true;
        
        // If this is a win, also set isGameFinishing (already set in propertyChange, but ensure it's set)
        if (forceWin) {
            isGameFinishing = true;
        }
        
        stopCountdown();
        BufferedImage image = canvas.exportImage();
        double timeTaken = timeLimitSeconds - timeLeftSeconds;
        
        String diffStr = difficultyLabel.getText().replace("Difficulty: ", "").trim();
        if (diffStr.isEmpty()) diffStr = "Medium";
        Difficulty difficulty = new Difficulty(diffStr); 
        
        String currentPrompt = promptLabel.getText().replace("Prompt: ", "").trim();
        
        // Note: The actual win/loss logic logic is now distributed. 
        // If forceWin is false (Give Up), we assume loss.
        // If win was detected by AI (handled in propertyChange), we will call this with detected outcome.
        // Actually, controller.executeGameResult takes everything.
        // The UseCase decides win/loss? No, Controller's executeGameResult currently has placeholders.
        // We need to pass the outcome.
        // The GameplayUseCase determines if we won via AI.
        // If we are here because 'Give Up' was clicked, we lost.
        // If we are here because Timer ran out, we lost.
        // If we are here because AI detected win, we won.
        
        // However, executeGameResult in Controller recalculates or takes 'hasWon' as input?
        // Looking at GameController: RecordGameInputData takes hasWon.
        // But executeGameResult signature does NOT take hasWon. It has a TODO inside.
        // Refactoring GameController to accept hasWon would be best, but let's see.
        // GameController.executeGameResult(...) calculates it or hardcodes it.
        // I should probably overload executeGameResult or modify it to accept hasWon.
        // For now, I will assume the controller will be updated or I should update it.
        // I ALREADY updated GameController? No, I added checkPrediction.
        // The executeGameResult still has `boolean hasWon = true; // TODO`.
        // I MUST fix GameController to accept hasWon, or pass it via a setter?
        // I'll update GameController.executeGameResult to take 'boolean hasWon'.
        
        // Wait, I can't edit GameController right now inside this file writing.
        // I will assume I will update GameController next.
        // So I will pass 'forceWin' (or the actual status) to the controller.
        
        controller.executeGameResult(image, currentPrompt, difficulty, timeTaken, timeLimitSeconds, forceWin);
        
        resetTool();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("[DEBUG] Game.propertyChange: Property = " + evt.getPropertyName());
        if ("state".equals(evt.getPropertyName())) {
            GameState state = (GameState) evt.getNewValue();
            if (state == null) {
                System.out.println("[DEBUG] Game.propertyChange: State is null!");
                return;
            }
            
            System.out.println("[DEBUG] Game.propertyChange: State hasWon = " + state.isHasWon());
            
            // Update predictions with progress bars
            List<Prediction> preds = state.getPredictions();
            String currentPrompt = promptLabel.getText().replace("Prompt: ", "").trim();
            boolean isCorrect = state.isHasWon();
            
            if (preds != null && !preds.isEmpty()) {
                predictionsPanel.removeAll();
                for (int i = 0; i < preds.size(); i++) {
                    Prediction pred = preds.get(i);
                    
                    // Check if this is the top prediction and it's correct
                    boolean isTopAndCorrect = (i == 0 && isCorrect && 
                        pred.getLabel().equalsIgnoreCase(currentPrompt));
                    
                    // Create a panel for each prediction
                    JPanel predPanel = new JPanel(new BorderLayout(5, 0));
                    predPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
                    
                    // Label with prediction name
                    JLabel label = new JLabel(pred.getLabel());
                    label.setPreferredSize(new Dimension(100, 20));
                    if (isTopAndCorrect) {
                        label.setForeground(new Color(0, 150, 0)); // Green color
                    }
                    predPanel.add(label, BorderLayout.WEST);
                    
                    // Progress bar showing confidence percentage
                    JProgressBar progressBar = new JProgressBar(0, 100);
                    int confidencePercent = (int) Math.round(pred.getConfidencePercent());
                    progressBar.setValue(confidencePercent);
                    progressBar.setStringPainted(true);
                    progressBar.setString(confidencePercent + "%");
                    progressBar.setPreferredSize(new Dimension(100, 20));
                    
                    // Color progress bar green if it's the correct top prediction
                    if (isTopAndCorrect) {
                        progressBar.setForeground(new Color(0, 200, 0)); // Green fill
                        progressBar.setBackground(new Color(200, 255, 200)); // Light green background
                    }
                    
                    predPanel.add(progressBar, BorderLayout.CENTER);
                    
                    predictionsPanel.add(predPanel);
                }
                predictionsPanel.revalidate();
                predictionsPanel.repaint();
                System.out.println("[DEBUG] Updated predictions panel with " + preds.size() + " predictions");
            } else {
                predictionsPanel.removeAll();
                predictionsPanel.revalidate();
                predictionsPanel.repaint();
                System.out.println("[DEBUG] Predictions list is null or empty.");
            }

            // Check win - add delay before finishing game
            if (state.isHasWon()) {
                // Guard: prevent duplicate win processing
                if (isGameFinishing) {
                    System.out.println("[DEBUG] Game.propertyChange: Already finishing game, ignoring duplicate win event");
                    return;
                }
                
                System.out.println("[DEBUG] Game.propertyChange: hasWon is true, waiting 2.5 seconds before finishing game");
                
                // Set flag and stop countdown immediately to prevent additional API checks
                isGameFinishing = true;
                stopCountdown();
                
                // Wait 2.5 seconds before finishing the game to show the green highlight
                javax.swing.Timer delayTimer = new javax.swing.Timer(2500, e -> {
                    System.out.println("[DEBUG] Delay complete, calling finishGame(true)");
                    finishGame(true);
                });
                delayTimer.setRepeats(false);
                delayTimer.start();
            } else {
                System.out.println("[DEBUG] Game.propertyChange: hasWon is false, not finishing game");
            }
        }
    }

    private Cursor createCursor(String path, String name) {
        try {
            Image img = new ImageIcon(getClass().getResource(path)).getImage();
            int size = 32;
            Image scaled = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            Point hotSpot = new Point(0, size-1);
            return Toolkit.getDefaultToolkit().createCustomCursor(scaled, hotSpot, name);
        } catch (Exception e) {
            return Cursor.getDefaultCursor();
        }
    }

    private Cursor getPencilCursor() {
        if (pencilCursor == null) {
            pencilCursor = createCursor("/cursors/pencil.png", "pencil");
        }
        return pencilCursor;
    }

    private Cursor getEraserCursor() {
        if (eraserCursor == null) {
            eraserCursor = createCursor("/cursors/eraser.png", "eraser");
        }
        return eraserCursor;
    }

    public void resetTool(){
        currentTool = Tool.PEN;
        currentToolCursor = getPencilCursor();
        if (canvas != null) {
            canvas.resetBrush();
            canvas.setCursor(currentToolCursor);
        }
    }

    public void setPromptText(String prompt) {
        this.promptLabel.setText("Prompt: " + prompt);
    }

    public void setTimerText(String timer) {
        this.timerLabel.setText("Time: " + timer);
    }

    public void setDifficultyText(String difficulty) {
        this.difficultyLabel.setText("Difficulty: " + difficulty);
    }

    public void clearCanvas(){
        canvas.clearCanvas();
        hasStartedDrawing = false;
        clearPredictions();
    }
    
    private void clearPredictions() {
        predictionsPanel.removeAll();
        predictionsPanel.revalidate();
        predictionsPanel.repaint();
    }

    private void initCountdownTimer() {
         countdownTimer = new Timer(1000, e -> {
            // Stop decrementing if game is finishing
            if (isGameFinishing) {
                System.out.println("[DEBUG] Timer tick skipped - game is finishing");
                return;
            }
            
            System.out.println("[DEBUG] Timer tick. Time left: " + timeLeftSeconds);
            timeLeftSeconds -= 1.0;
            
            // API Check every 3 seconds (when timeLeftSeconds % 3 == 0)
            // Skip API check if game is already finishing to prevent duplicate calls
            if (hasStartedDrawing && timeLeftSeconds > 0 && (int)Math.round(timeLeftSeconds) % 3 == 0 && !isGameFinishing) {
                System.out.println("[DEBUG] Checking API...");
                String currentPrompt = promptLabel.getText().replace("Prompt: ", "").trim();
                
                // Save drawing for debugging
                BufferedImage debugImage = canvas.exportImage();
                File debugDir = new File("src/main/resources/debugging");
                if (!debugDir.exists()) {
                    debugDir.mkdirs();
                }
                String fileName = UUID.randomUUID().toString() + ".png";
                File outputFile = new File(debugDir, fileName);
                try {
                    ImageIO.write(debugImage, "PNG", outputFile);
                    System.out.println("[DEBUG] Saved drawing to: " + outputFile.getAbsolutePath());
                } catch (IOException ex) {
                    System.err.println("[DEBUG] Error saving debug image: " + ex.getMessage());
                }
                
                controller.checkPrediction(canvas.exportImage(), currentPrompt);
            } else {
                 System.out.println("[DEBUG] Skipping API check...");
            }

            if (timeLeftSeconds <= 0) {
                System.out.println("[DEBUG] Time over.");
                timeLeftSeconds = 0;
                updateTimerLabel();
                countdownTimer.stop();
                finishGame(false); // Time over -> Loss
            } else {
                updateTimerLabel();
            }
        });
    }
    
    private void stopCountdown() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
    }

    public void startCountdown() {
        if (countdownTimer != null && !countdownTimer.isRunning()) {
            countdownTimer.start();
        }
    }

    public void setTimeLimitSeconds(double seconds) {
        System.out.println("[DEBUG] setTimeLimitSeconds called with: " + seconds);
        this.timeLimitSeconds = seconds;
        this.timeLeftSeconds = seconds;
        updateTimerLabel();
        clearPredictions();
        hasStartedDrawing = false;

        if (countdownTimer != null) {
             stopCountdown(); // ensure old one stops
        }
        initCountdownTimer(); // Creates new countdown timer
    }

    private void updateTimerLabel() {
        timerLabel.setText("Time left: " + (int) Math.ceil(timeLeftSeconds) + " s");
    }

    public void resetCompletely(){
        canvas.clearCanvas();
        resetTool();
        timeLimitSeconds = 0;
        timeLeftSeconds = 0;
        promptLabel.setText("Prompt: ");
        difficultyLabel.setText("Difficulty: ");
        timerLabel.setText("Timer: ");
        clearPredictions();
        hasStartedDrawing = false;
        isGameFinishing = false; // Reset flag for new game session
        hasFinishedGame = false; // Reset flag for new game session
    }

    public void resetForNewGame(){
        clearCanvas();
        resetTool();
        clearPredictions();
        hasStartedDrawing = false;
        isGameFinishing = false; // Reset flag for new game
        hasFinishedGame = false; // Reset flag for new game
    }

    public void resetForRetry(){
        // This function is needed as we need to reset the time. Normally when we press "New Game" from main menu,
        // we reset the time limit and start the countdown already.
        clearCanvas();
        resetTool();
        timeLeftSeconds = timeLimitSeconds;
        updateTimerLabel();
        startCountdown();
        clearPredictions();
        hasStartedDrawing = false;
        isGameFinishing = false; // Reset flag for retry
        hasFinishedGame = false; // Reset flag for retry
    }

    private class DrawingCanvas extends JPanel {
        private final List<List<Point>> strokes = new ArrayList<>();
        private final List<Color> strokeColors = new ArrayList<>();
        private final List<Integer> strokeSizes = new ArrayList<>();
        private List<Point> currentStroke = null;

        private Color brushColor = Color.BLACK;
        private int brushSize = 3;

        public DrawingCanvas() {
            setBackground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    // Show default cursor if game is finishing
                    if (isGameFinishing) {
                        setCursor(Cursor.getDefaultCursor());
                    } else if (currentToolCursor != null){
                        setCursor(currentToolCursor);
                    } else{
                        setCursor(Cursor.getDefaultCursor());
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // Don't allow drawing if game is finishing
                    if (isGameFinishing) {
                        return;
                    }
                    
                    hasStartedDrawing = true; // Start checking API
                    // Start timer if not started? 
                    // Usually timer starts when game enters? 
                    // Project description: "while that number counts down, every second after the player starts drawing..."
                    // So timer is already running, but API calls start after drawing.
                    if (countdownTimer != null && !countdownTimer.isRunning()) {
                        countdownTimer.start();
                    }

                    switch (currentTool) {
                        case PEN:
                            brushColor = Color.BLACK;
                            brushSize = 10;
                            break;
                        case ERASER:
                            brushColor = Color.WHITE;
                            brushSize = 20;
                            break;
                        case NONE:
                            return;
                    }

                    currentStroke = new ArrayList<>();
                    currentStroke.add(e.getPoint());
                    strokes.add(currentStroke);
                    strokeColors.add(brushColor);
                    strokeSizes.add(brushSize);
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    currentStroke = null;
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    // Don't allow drawing if game is finishing
                    if (isGameFinishing) {
                        return;
                    }
                    
                    if (currentStroke != null) {
                        currentStroke.add(e.getPoint());
                        repaint();
                    }
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            for (int s = 0; s < strokes.size(); s++) {
                List<Point> stroke = strokes.get(s);
                Color color = strokeColors.get(s);
                int size = strokeSizes.get(s);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(size));

                for (int i = 1; i < stroke.size(); i++) {
                    Point p1 = stroke.get(i - 1);
                    Point p2 = stroke.get(i);
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }

        public void clearCanvas() {
            strokes.clear();
            strokeColors.clear();
            strokeSizes.clear();
            repaint();
        }

        public BufferedImage exportImage() {
            // First, render the canvas at its current size
            BufferedImage fullSizeImage = new BufferedImage(
                    getWidth(),
                    getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2 = fullSizeImage.createGraphics();
            paintAll(g2);
            g2.dispose();
            
            // Save BEFORE downsampling (full-size image)
            String uuid = UUID.randomUUID().toString();
            saveImageToFile(fullSizeImage, "canvas_before_downsample", uuid, "Canvas BEFORE downsampling");
            
            // Sample down to 28x28 using min pooling
            // Min pooling takes the minimum (darkest) value from each pool region
            // This preserves black drawing lines better than nearest neighbor
            BufferedImage resizedImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
            int sourceWidth = fullSizeImage.getWidth();
            int sourceHeight = fullSizeImage.getHeight();
            
            // Calculate pool size
            double poolHeight = (double) sourceHeight / 28;
            double poolWidth = (double) sourceWidth / 28;
            
            // Min pooling: take minimum value from each pool region
            for (int y = 0; y < 28; y++) {
                for (int x = 0; x < 28; x++) {
                    // Calculate source region bounds
                    int startY = (int) (y * poolHeight);
                    int endY = (int) ((y + 1) * poolHeight);
                    int startX = (int) (x * poolWidth);
                    int endX = (int) ((x + 1) * poolWidth);
                    
                    // Ensure bounds are within image
                    endY = Math.min(endY, sourceHeight);
                    endX = Math.min(endX, sourceWidth);
                    
                    // Find minimum RGB value in the pool region
                    int minRGB = 0xFFFFFFFF; // Start with white (max value)
                    for (int sy = startY; sy < endY; sy++) {
                        for (int sx = startX; sx < endX; sx++) {
                            int rgb = fullSizeImage.getRGB(sx, sy);
                            // Compare brightness (lower is darker/black)
                            int brightness = getBrightness(rgb);
                            int minBrightness = getBrightness(minRGB);
                            if (brightness < minBrightness) {
                                minRGB = rgb;
                            }
                        }
                    }
                    resizedImage.setRGB(x, y, minRGB);
                }
            }
            
            // Save AFTER downsampling (28x28 image)
            saveImageToFile(resizedImage, "canvas_after_downsample", uuid, "Canvas AFTER downsampling");
            
            return resizedImage;
        }
        
        /**
         * Calculates the brightness of an RGB pixel.
         * 
         * @param rgb The RGB pixel value
         * @return Brightness value (0-255, lower is darker)
         */
        private int getBrightness(int rgb) {
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;
            // Use luminance formula for grayscale conversion
            return (int) (0.299 * r + 0.587 * g + 0.114 * b);
        }
        
        /**
         * Saves an image to the debugging directory with the given filename prefix and UUID.
         * 
         * @param image The BufferedImage to save
         * @param filenamePrefix The prefix for the filename
         * @param uuid The UUID to append to the filename
         * @param description Description for debug logging
         */
        private void saveImageToFile(BufferedImage image, String filenamePrefix, String uuid, String description) {
            try {
                File debugDir = new File("src/main/resources/debugging");
                if (!debugDir.exists()) {
                    debugDir.mkdirs();
                }
                String fileName = filenamePrefix + "_" + uuid + ".png";
                File outputFile = new File(debugDir, fileName);
                ImageIO.write(image, "png", outputFile);
                System.out.println("[DEBUG] Saved " + description + " to: " + outputFile.getAbsolutePath());
            } catch (Exception e) {
                System.err.println("[DEBUG] Error saving " + description + ": " + e.getMessage());
            }
        }
        public void setBrushColor(Color color) {
            this.brushColor = color;
        }

        public void setBrushSize(int size) {
            this.brushSize = size;
        }
        public  void resetBrush(){
            this.brushColor = Color.BLACK;
            this.brushSize = 10;
        }
    }
}
