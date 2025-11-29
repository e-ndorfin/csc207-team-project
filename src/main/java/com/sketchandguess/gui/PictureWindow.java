package com.sketchandguess.gui;

import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowController;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowState;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowViewModel;
import com.sketchandguess.entities.GameRecord;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JOptionPane;

public class PictureWindow extends JFrame implements PropertyChangeListener {

    private final GalleryWindowViewModel GalleryWindowViewModel;
    private final GalleryWindowController GalleryWindowController;

    private JLabel imageLabel;
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JLabel errorLabel;
    private JButton saveButton;
    private JButton deleteButton;

    public PictureWindow(GalleryWindowViewModel GalleryWindowViewModel,
                         GalleryWindowController GalleryWindowController) {

        this.GalleryWindowViewModel = GalleryWindowViewModel;
        this.GalleryWindowController = GalleryWindowController;
        this.GalleryWindowViewModel.addPropertyChangeListener(this);

        setTitle("Picture");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        layoutComponents();
        updateFromState();
    }

    private void initComponents() {
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel = new JLabel();
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));

        infoLabel = new JLabel();

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);

        saveButton = new JButton("Save Image");
        saveButton.addActionListener(e -> {
            GalleryWindowState state = GalleryWindowViewModel.getState();
            GameRecord record = state.getCurrentRecord();

            if (record != null) {
                GalleryWindowController.saveImage();
            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            GalleryWindowState state = GalleryWindowViewModel.getState();
            GameRecord record = state.getCurrentRecord();

            if (record != null) {
                int result = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this image?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (result == JOptionPane.YES_OPTION) {
                    GalleryWindowController.deleteGame();
                    this.dispose();
                }
            }
        });
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(titleLabel);
        infoPanel.add(infoLabel);
        infoPanel.add(errorLabel);

        JScrollPane imageScroll = new JScrollPane(imageLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        add(infoPanel, BorderLayout.NORTH);
        add(imageScroll, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateFromState() {
        GalleryWindowState state = GalleryWindowViewModel.getState();
        GameRecord record = state.getCurrentRecord();

        if (record != null) {
            titleLabel.setText("Picture Preview");
            infoLabel.setText(record.getPrompt());

            String imagePath = record.getImagePath();

            if (imagePath != null) {
                ImageIcon icon = new ImageIcon(imagePath);

                Image scaled = icon.getImage().getScaledInstance(
                        600, -1, Image.SCALE_SMOOTH);

                imageLabel.setIcon(new ImageIcon(scaled));
                imageLabel.setText(null);
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("No image available");
            }

        } else {
            titleLabel.setText("");
            infoLabel.setText("");
            imageLabel.setIcon(null);
            imageLabel.setText("No picture selected");
        }

        errorLabel.setText(state.getErrorMessage());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateFromState();
    }
}
