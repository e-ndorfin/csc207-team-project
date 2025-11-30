package com.sketchandguess.gui;

import com.sketchandguess.interface_adapters.menu.MenuController;
import com.sketchandguess.interface_adapters.menu.MenuViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private final MenuController controller;
    private final MenuViewModel viewModel;

    public MainMenu(MenuController controller, MenuViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        
        // Title
        JLabel titleLabel = new JLabel("Sketch and Guess", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 0, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        
        JButton playButton = new JButton("Play Game");
        JButton galleryButton = new JButton("Gallery");
        JButton settingsButton = new JButton("Settings");
        JButton exitButton = new JButton("Exit");
        
        // Style buttons
        playButton.setFont(new Font("Arial", Font.PLAIN, 16));
        galleryButton.setFont(new Font("Arial", Font.PLAIN, 16));
        settingsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        exitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        
        buttonPanel.add(playButton);
        buttonPanel.add(galleryButton);
        buttonPanel.add(settingsButton);
        buttonPanel.add(exitButton);
        
        add(buttonPanel, BorderLayout.CENTER);
        
        // Button actions
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startNewGame();
            }
        });
        
        galleryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showGallery();
            }
        });
        
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showSettings();
            }
        });
        
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.exitApplication();
            }
        });
    }
}
