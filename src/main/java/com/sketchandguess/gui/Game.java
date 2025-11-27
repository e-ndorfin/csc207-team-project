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
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.sketchandguess.entities.Difficulty;
import com.sketchandguess.interface_adapters.game.GameController;


public class Game extends JPanel {
    private final Application app;
    private final GameController controller;

    private String prompt; // current game's prompt(using controller to generate random prompt)
    private JLabel promptLabel;
    private JLabel difficultyLabel;
    private String difficultyText;
    private JLabel timerLabel;
    private double timeLimitSeconds; // current game's time limit（from controller)
    private double timeLeftSeconds; // current left time
    private boolean hasWon;
    private javax.swing.Timer countdownTimer;
    private DrawingCanvas canvas;

    private Cursor pencilCursor;
    private Cursor eraserCursor;
    private Cursor currentToolCursor = null;

    private enum Tool { NONE, PEN, ERASER }
    private Tool currentTool = Tool.PEN;

    public Game(Application app, GameController controller) {
        this.app = app;
        this.controller = controller;
        setLayout(new BorderLayout());
        canvas = new DrawingCanvas();
        add(canvas, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel topright = new JPanel(new GridLayout(3, 1)); // for prompt and timer

        promptLabel = new JLabel("Prompt: "); // using setprompttext update
        timerLabel = new JLabel("Time left: "); // using settimetext update
        difficultyLabel = new JLabel("Difficulty: "); // using setdifficultytext update

        topright.add(promptLabel);
        topright.add(timerLabel);
        topright.add(difficultyLabel);
        rightPanel.add(topright, BorderLayout.NORTH);

        JPanel bottomright = new JPanel(new GridLayout(5, 1, 10, 10)); //button area
        JButton doneButton = new JButton("Done");
        JButton backButton = new JButton("Back");
        JButton clearButton = new JButton("Clear");
        JButton penButton = new JButton("Pen");
        JButton eraserButton = new JButton("Eraser");

        bottomright.add(clearButton);
        bottomright.add(backButton);
        bottomright.add(doneButton);
        bottomright.add(eraserButton);
        bottomright.add(penButton);
        rightPanel.add(bottomright, BorderLayout.SOUTH);

        rightPanel.setPreferredSize(new Dimension(250, 0));
        add(rightPanel, BorderLayout.EAST);

        doneButton.addActionListener(e -> {
            BufferedImage image = canvas.exportImage();
            double timeTaken = timeLimitSeconds - timeLeftSeconds;
            // Parse difficulty string to Difficulty object
            String diffStr = difficultyLabel.getText().replace("Difficulty: ", "").trim();
            if (diffStr.isEmpty()) {
                diffStr = "Medium";
            }
            Difficulty difficulty = new Difficulty(diffStr); 
            
            String currentPrompt = promptLabel.getText().replace("Prompt: ", "").trim();
            
            controller.executeGameResult(image, currentPrompt, difficulty, timeTaken, timeLimitSeconds);
            
            stopCountdown();
            resetTool();
        });

        backButton.addActionListener(e -> {
            stopCountdown();
            resetCompletely();
            app.showMainmenu();}
        );

        clearButton.addActionListener(e -> {
            canvas.clearCanvas();
            resetTool();
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

    private Cursor createCursor(String path, String name) {
        Image img = new ImageIcon(getClass().getResource(path)).getImage();
        int size = 32;
        Image scaled = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        Point hotSpot = new Point(0, size-1);
        return Toolkit.getDefaultToolkit().createCustomCursor(scaled, hotSpot, name);
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

    /** update prompt */
    public void setPromptText(String prompt) {
        this.promptLabel.setText("Prompt: " + prompt);
    }

    /** update the time left */
    public void setTimerText(String timer) {
        this.timerLabel.setText("Time: " + timer);
    }

    public void setDifficultyText(String difficulty) {
        this.difficultyLabel.setText("Difficulty: " + difficulty);
    }

    public void clearCanvas(){
        canvas.clearCanvas();
    }


    private void initCountdownTimer() {
         countdownTimer = new Timer(1000, e -> {
            timeLeftSeconds -= 1.0;
            if (timeLeftSeconds <= 0) {
                timeLeftSeconds = 0;
                updateTimerLabel();
                countdownTimer.stop();
                // TODO: tell controller time over
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
        this.timeLimitSeconds = seconds;
        this.timeLeftSeconds = seconds;
        updateTimerLabel();

        if (countdownTimer != null) {
            initCountdownTimer();
        }
        updateTimerLabel();
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
    }

    public void resetForNewGame(){
        clearCanvas();
        resetTool();
    }

    public void resetForRetry(){
        canvas.clearCanvas();
        resetTool();
        timeLeftSeconds = timeLimitSeconds;
        updateTimerLabel();
        startCountdown();
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
                    if (currentToolCursor != null){
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
                    switch (currentTool) {
                        case PEN:
                            brushColor = Color.BLACK;
                            brushSize = 4;
                            break;
                        case ERASER:
                            brushColor = Color.WHITE;
                            brushSize = 10;
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
            BufferedImage image = new BufferedImage(
                    getWidth(),
                    getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2 = image.createGraphics();
            paintAll(g2);
            g2.dispose();
            return image;
        }
        public void setBrushColor(Color color) {
            this.brushColor = color;
        }

        public void setBrushSize(int size) {
            this.brushSize = size;
        }
        public  void resetBrush(){
            this.brushColor = Color.BLACK;
            this.brushSize = 3;
        }
    }
}

