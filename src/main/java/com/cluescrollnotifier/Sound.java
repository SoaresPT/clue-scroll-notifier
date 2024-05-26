package com.cluescrollnotifier;

public enum Sound {
    CASKET("clue1.wav"),
    CASKET2("clue2.wav");

    private final String fileName;

    Sound(String fileName) {
        this.fileName = fileName;
    }

    String getFileName() {
        return fileName;
    }

    public static final Sound[] CASKET_SOUNDS = new Sound[]{
            Sound.CASKET,
            Sound.CASKET2
    };
}
