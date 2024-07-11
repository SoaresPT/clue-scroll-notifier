package com.cluescrollnotifier;

import java.io.InputStream;

public final class FileManager {

    private static final String SOUND_FILE = "/clue.wav";

    private FileManager() {
        // Private constructor to prevent instantiation
    }

    public static InputStream getSoundStream() {
        InputStream stream = FileManager.class.getResourceAsStream(SOUND_FILE);
        if (stream == null) {
            throw new IllegalArgumentException("Sound file not found: " + SOUND_FILE);
        }
        return stream;
    }
}