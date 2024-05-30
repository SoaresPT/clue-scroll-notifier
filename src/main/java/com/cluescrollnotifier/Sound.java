package com.cluescrollnotifier;

public enum Sound {
    CLUE("clue.wav");
    private final String fileName;

    Sound(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}