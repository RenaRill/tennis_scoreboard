package com.tennisscoreboard.model;

public enum Points {
    LOVE("0"),
    FIFTEEN( "15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD");

    private String score;

    Points(String score) {
        this.score = score;
    }
}