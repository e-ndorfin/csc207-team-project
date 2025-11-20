package com.sketchandguess.gui;

import com.sketchandguess.database.DataBase;
import com.sketchandguess.database.GameDataBase;
import com.sketchandguess.entities.GameRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private final JPanel SearchBar = new JPanel();
    private final JButton SearchButton = new JButton("Search");
    private final JButton ClearButton = new JButton("Clear");
    private Map<ImageIcon, GameRecord> IconView;
    private JList RecordList;
    private DefaultListModel<GameRecord> RecordListModel;
    private JScrollPane GalleryScrollPane = new JScrollPane();


    public Gallery() {
        this.MainDataBase = new GameDataBase();
        this.CurrentDataBase = MainDataBase;

        this.SearchBar.add(SearchBarField);
        this.SearchBar.add(SearchButton);
        this.SearchBar.add(ClearButton);
        add(SearchBar, BorderLayout.NORTH);

        this.UpdateIconView();
        this.UpdateListView();

        RecordList = new JList(RecordListModel);
        RecordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        RecordList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        RecordList.setVisibleRowCount(2);
        GalleryScrollPane.add(RecordList);

        if (! CurrentDataBase.GameData.isEmpty()) {
            add(GalleryScrollPane, BorderLayout.CENTER);
        }

        SearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentDataBase = MainDataBase.SearchWord(SearchBarField.getText());
                UpdateListView();
            }
                                       });

        ClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentDataBase = MainDataBase;
                UpdateListView();
            }
        });
        {

        }


    }
    private void UpdateListView() {
        RecordListModel = new DefaultListModel<GameRecord>();
        if (! CurrentDataBase.GameData.isEmpty()) {
            for (GameRecord G : CurrentDataBase.GameData) {
                RecordListModel.addElement(G);
            }
            RecordList = new JList<>(RecordListModel);
        } else {
            JLabel EmptyLabel = new JLabel(EmptyGallery, SwingConstants.CENTER);
            add(EmptyLabel, BorderLayout.CENTER);
        }
        UpdateIconView();

    }

    public void UpdateIconView() {
        this.IconView = new HashMap<ImageIcon, GameRecord>();
        for (GameRecord G : this.CurrentDataBase.GameData) {
            this.IconView.put(new ImageIcon(G.getImagePath()), G);
        }
    }

}
