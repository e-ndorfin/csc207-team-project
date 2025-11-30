package com.sketchandguess.usecases.selectgame;

import com.sketchandguess.entities.GameRecord;

public class SelectGameRecordUseCase implements SelectGameRecordInputBoundary {

    private final SelectGameRecordOutputBoundary presenter;

    public SelectGameRecordUseCase(SelectGameRecordOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(GameRecord record) {
        if (record == null) {
            presenter.prepareFailView("Selected record is invalid.");
        } else {
            presenter.prepareSuccessView(record);
        }
    }
}

