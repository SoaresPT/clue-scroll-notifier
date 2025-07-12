package com.cluescrollnotifier;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.audio.AudioPlayer;

@Singleton
@Slf4j
public class SoundEngine {

    @Inject
    private ClueScrollNotifierConfig config;

    @Inject
    private AudioPlayer audioPlayer;

    public void playClip(Sound sound) {
        float gain = 20f * (float) Math.log10(config.announcementVolume() / 100f);

        try (InputStream stream = new BufferedInputStream(FileManager.getSoundStream(sound))) {
            audioPlayer.play(stream, gain);
        } catch (FileNotFoundException e) {
            log.warn("Sound file not found for {}", sound, e);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            log.warn("Failed to play sound {}", sound, e);
        }
    }
}