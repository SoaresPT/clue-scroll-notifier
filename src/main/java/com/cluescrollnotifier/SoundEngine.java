package com.cluescrollnotifier;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.*;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class SoundEngine {

    @Inject
    private ClueScrollNotifierConfig config;

    private Clip clip = null;

    private boolean loadClip() {
        try (InputStream stream = new BufferedInputStream(FileManager.getSoundStream());
             AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream)) {
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return true;
        } catch (UnsupportedAudioFileException e) {
            log.warn("Failed to load sound file: Unsupported format", e);
        } catch (IOException e) {
            log.warn("Failed to load sound file: IO Exception", e);
        } catch (LineUnavailableException e) {
            log.warn("Failed to load sound file: Line unavailable", e);
        }
        return false;
    }

    public void playClip() {
        if (clip == null || !clip.isOpen()) {
            if (clip != null) {
                clip.close();
            }

            if (!loadClip()) {
                return;
            }
        }

        if (clip.isRunning()) {
            clip.stop();
        }

        clip.setFramePosition(0);  // Rewind to the beginning
        setVolume(config.announcementVolume());

        log.debug("Playing sound");
        clip.start();  // Start the clip
    }

    private void setVolume(int volumePercentage) {
        if (clip != null) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float gain = calculateGain(volumeControl, volumePercentage);
            volumeControl.setValue(gain);
        }
    }

    private float calculateGain(FloatControl volumeControl, int volumePercentage) {
        float range = volumeControl.getMaximum() - volumeControl.getMinimum();
        float gain = (range * volumePercentage / 100f) + volumeControl.getMinimum();
        return Math.min(Math.max(gain, volumeControl.getMinimum()), volumeControl.getMaximum());
    }

    public void close() {
        if (clip != null && clip.isOpen()) {
            clip.close();
        }
    }
}