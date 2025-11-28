package com.sketchandguess.database;

public class SearchObject {
    private final String query;

    public SearchObject(String query) {
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }
}
