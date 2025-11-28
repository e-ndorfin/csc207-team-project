package com.sketchandguess.interface_adapters.gallery_window;

import com.sketchandguess.entities.GameRecord;

public class GalleryWindowState {
    private GameRecord currentRecord = null;
    private String errorMessage = "";
    private String dateText = "";
    private String promptText = "";
    private String outcomeText = "";

    public GalleryWindowState() {
    }

    public GameRecord getCurrentRecord() {
        return currentRecord;
    }

    public void setCurrentRecord(GameRecord currentRecord) {
        this.currentRecord = currentRecord;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public String getOutcomeText() {
        return outcomeText;
    }

    public void setOutcomeText(String outcomeText) {
        this.outcomeText = outcomeText;
    }
}
