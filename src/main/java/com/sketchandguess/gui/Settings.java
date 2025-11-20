package com.sketchandguess.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends JPanel {
    public Settings() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Settings - Coming Soon!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Will be handled by Application
                JOptionPane.showMessageDialog(null, "Settings feature coming soon!");
            }
        });
        
        add(titleLabel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}
