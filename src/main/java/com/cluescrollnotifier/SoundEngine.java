package com.cluescrollnotifier;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Singleton
@Slf4j
public class SoundEngine {

    @Inject
    private ClueScrollNotifierConfig config;

    private static final long CLIP_MTIME_UNLOADED = -2;
    private long lastClipMTime = CLIP_MTIME_UNLOADED;
    private Clip clip = null;

    private boolean loadClip(Sound sound) {
        try (InputStream stream = new BufferedInputStream(FileManager.getSoundStream(sound))) {
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream)) {
                clip.open(audioInputStream);
            }
            return true;
        } catch (UnsupportedAudioFileException e) {
            log.warn("Unsupported audio file: " + sound.getFileName(), e);
        } catch (IOException e) {
            log.warn("I/O error loading sound: " + sound.getFileName(), e);
        } catch (LineUnavailableException e) {
            log.warn("Line unavailable for sound: " + sound.getFileName(), e);
        }
        return false;
    }

    public void playClip(Sound sound) {
        long currentMTime = System.currentTimeMillis();
        if (clip == null || currentMTime != lastClipMTime || !clip.isOpen()) {
            if (clip != null && clip.isOpen()) {
                clip.close();
            }

            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                lastClipMTime = CLIP_MTIME_UNLOADED;
                log.warn("Failed to get audio clip for sound: " + sound.getFileName(), e);
                return;
            }

            lastClipMTime = currentMTime;
            if (!loadClip(sound)) {
                return;
            }
        }

        // User configurable volume
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float gain = 20f * (float) Math.log10(config.announcementVolume() / 100f);
        gain = Math.min(gain, volume.getMaximum());
        gain = Math.max(gain, volume.getMinimum());
        volume.setValue(gain);

        // Using loop instead of start + setFramePosition to avoid race condition
        clip.loop(0);
    }

    public void close() {
        if (clip != null && clip.isOpen()) {
            clip.close();
        }
    }
}