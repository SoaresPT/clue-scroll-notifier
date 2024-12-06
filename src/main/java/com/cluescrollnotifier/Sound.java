package com.cluescrollnotifier;

public enum Sound {
    CLUE1("clue1.wav"),
    CLUE2("clue2.wav"),
    CLUE3("clue3.wav"),
    CLUE4("clue4.wav"),
    CLUE5("clue5.wav");

    private final String fileName;

    Sound(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}