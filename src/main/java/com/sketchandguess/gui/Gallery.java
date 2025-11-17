package com.sketchandguess.gui;

import com.sketchandguess.database.DataBase;
import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.entities.GameRecord;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

// TODO ensure this does not call usecase methods, if it does, use a controller
public class Gallery extends JPanel {
    private final String ViewName = "Drawing Gallery";
    private final String EmptyGallery = "No Pictures Found";
    // this database represents the "main" database of images we are drawing from; it will be the database shown by default
    public final GameDataBase MainDataBase;
    // this database represents the current database being shown. Usually, this is the MainDataBase, but it will change when the search bar is used.
    public GameDataBase CurrentDataBase;

    private final JTextField SearchBarField = new JTextField(15);
    private final JLabel SearchBarLabel = new JLabel("Search:");
    private final JPanel SearchBar = new JPanel();
    private Map<ImageIcon, GameRecord> IconView;

    public Gallery(GameDataBase MainDB) {
        this.MainDataBase = MainDB;
        this.CurrentDataBase = MainDB;
        this.SearchBar.add(SearchBarLabel);
        this.SearchBar.add(SearchBarField);
        this.UpdateIconView();
    }

    public void UpdateIconView() {
        this.IconView = new HashMap<ImageIcon, GameRecord>();
        for (GameRecord G : this.CurrentDataBase.GameData) {
            this.IconView.put(new ImageIcon(G.getImagePath()), G);
        }
    }

    SearchBarField.addActionListener;


}