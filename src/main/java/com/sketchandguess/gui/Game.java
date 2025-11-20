package com.sketchandguess.gui;



import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;


public class Game extends JPanel {
    private final Application app;
    private final RecordGameController controller;

    private String prompt; // current game's prompt(using controller to generate random prompt)
    private JLabel promptLabel;
    private JLabel difficultyLabel;
    private String difficultyText;
    private JLabel timerLabel;
    private double timeLimitSeconds; // current game's time limit（from controller)
    private double timeLeftSeconds; // current left time
    private javax.swing.Timer countdownTimer;
    private DrawingCanvas canvas;

    public Game(Application app, RecordGameController controller) {
        this.app = app;
        this.controller = controller;
        setLayout(new BorderLayout());
        canvas = new DrawingCanvas();

        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel topright = new JPanel(new GridLayout(2, 1)); // for prompt and timer
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

        doneButton.addActionListener(e -> {
            BufferedImage image = canvas.exportImage();
            controller.onDoneButtonClicked(image);
        });
        backButton.addActionListener(e -> app.showMainmenu());
        clearButton.addActionListener(e -> canvas.clearCanvas());
        penButton.addActionListener(e -> {canvas.setBrushColor(Color.BLACK); canvas.setBrushSize(4);});
        eraserButton.addActionListener(e -> {canvas.setBrushColor(Color.WHITE); canvas.setBrushSize(10);});

        rightPanel.setPreferredSize(new Dimension(250, 0));
        add(canvas, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
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


    public void startCountdown() {
        if (countdownTimer != null && !countdownTimer.isRunning()) {
            countdownTimer.start();
        }
    }

    public void setTimeLimitSeconds(double seconds) {
        this.timeLimitSeconds = seconds;
        this.timeLeftSeconds = seconds;
        updateTimerLabel();
    }

    private void updateTimerLabel() {
        timerLabel.setText("Time left: " + (int) Math.ceil(timeLeftSeconds) + " s");
    }


    private static class DrawingCanvas extends JPanel {

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
                public void mousePressed(MouseEvent e) {
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
    }
}

