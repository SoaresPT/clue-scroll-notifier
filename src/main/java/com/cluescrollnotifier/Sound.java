package com.cluescrollnotifier;

public enum Sound {
    CLUE("clue1.wav"),
    CLUE2("clue2.wav");

    private final String fileName;

    Sound(String fileName) {
        this.fileName = fileName;
    }

    String getFileName() {
        return fileName;
    }

    public static final Sound[] CASKET_SOUNDS = new Sound[]{
            Sound.CLUE,
            Sound.CLUE2
    };
}
