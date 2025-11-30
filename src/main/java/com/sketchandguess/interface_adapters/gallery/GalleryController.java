package com.sketchandguess.interface_adapters.gallery;

import com.sketchandguess.entities.GameRecord;
import com.sketchandguess.gui.MainMenu;
import com.sketchandguess.interface_adapters.ViewManagerModel;
import com.sketchandguess.interface_adapters.gallery_window.GalleryWindowViewModel;
import com.sketchandguess.usecases.gallery.RetrieveGamesInputBoundary;
import com.sketchandguess.usecases.gallery.RetrieveGamesInputData;
import com.sketchandguess.usecases.gallery.SearchGamesInputBoundary;
import com.sketchandguess.usecases.gallery.SearchGamesInputData;

public class GalleryController {
    private final RetrieveGamesInputBoundary retrieveGamesInputBoundary;
    private final SearchGamesInputBoundary searchGamesInputBoundary;
    private ViewManagerModel viewManagerModel;

    public GalleryController(RetrieveGamesInputBoundary retrieveGamesInputBoundary, SearchGamesInputBoundary searchGamesInputBoundary, ViewManagerModel viewManagerModel) {
        this.retrieveGamesInputBoundary = retrieveGamesInputBoundary;
        this.searchGamesInputBoundary = searchGamesInputBoundary;
        this.viewManagerModel = viewManagerModel;
    }
    public void refreshGallery() {
        RetrieveGamesInputData data = new RetrieveGamesInputData();
        retrieveGamesInputBoundary.execute(data);
    }

    public void searchGames(String query) {
        SearchGamesInputData data = new SearchGamesInputData(query);
        searchGamesInputBoundary.execute(data);
    }

    public void clearSearch () {
        RetrieveGamesInputData data = new RetrieveGamesInputData();
        retrieveGamesInputBoundary.execute(data);
    }

    public void selectGameRecord (GameRecord record) {
        viewManagerModel.setState(GalleryWindowViewModel.VIEW_NAME); //TODO not sure how to do this one
        viewManagerModel.firePropertyChange("state");
    }

    public void returnToMainMenu () {
        viewManagerModel.setState(MenuViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChange("state");
    }
}
