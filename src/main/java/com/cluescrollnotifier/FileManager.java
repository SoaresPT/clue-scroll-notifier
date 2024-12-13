package com.cluescrollnotifier;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileManager {
    private static final Logger log = LoggerFactory.getLogger(FileManager.class);
    private static final Path DOWNLOAD_DIR = new File(RuneLite.RUNELITE_DIR.getPath() + File.separator + "clue-scroll-notifier").toPath();

    public static void initialize() {
        try {
            Files.createDirectories(DOWNLOAD_DIR);
        } catch (IOException e) {
            log.error("Could not create download directory", e);
            return;
        }

        Set<String> filesPresent = getFilesPresent();
        Set<String> desiredFiles = getDesiredSoundList().stream()
                .map(Sound::getFileName)
                .collect(Collectors.toSet());

        for (String fileName : desiredFiles) {
            Path outputPath = DOWNLOAD_DIR.resolve(fileName);
            if (Files.exists(outputPath)) {
                filesPresent.remove(fileName);
                continue;
            }

            try (InputStream resourceStream = FileManager.class.getResourceAsStream("/" + fileName)) {
                if (resourceStream != null) {
                    Files.copy(resourceStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    log.error("Clue Scroll Notifier could not find resource: {}", fileName);
                }
            } catch (IOException e) {
                log.error("Clue Scroll Notifier could not copy sound: {}", fileName, e);
            }
        }

        for (String filename : filesPresent) {
            File toDelete = new File(DOWNLOAD_DIR.toFile(), filename);
            if (!toDelete.delete()) {
                log.warn("Failed to delete file: {}", filename);
            }
        }
    }

    private static Set<String> getFilesPresent() {
        File[] downloadDirFiles = DOWNLOAD_DIR.toFile().listFiles();
        if (downloadDirFiles == null || downloadDirFiles.length == 0) {
            return new HashSet<>();
        }

        return Arrays.stream(downloadDirFiles)
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    private static Set<Sound> getDesiredSoundList() {
        return Arrays.stream(Sound.values()).collect(Collectors.toSet());
    }

    public static InputStream getSoundStream(Sound sound) throws FileNotFoundException {
        return new FileInputStream(new File(DOWNLOAD_DIR.toFile(), sound.getFileName()));
    }
}