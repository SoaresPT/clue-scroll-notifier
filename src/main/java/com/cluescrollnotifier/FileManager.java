package com.cluescrollnotifier;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;
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
import java.util.stream.Stream;

@Slf4j
public class FileManager {
    private static final Path DOWNLOAD_DIR = Paths.get(RuneLite.RUNELITE_DIR.getPath(), "clue-scroll-notifier");

    public static void initialize() {
        createDownloadDirectory();
        Set<String> filesPresent = getFilesPresent();
        Set<String> desiredFiles = getDesiredSoundList().stream()
                .map(Sound::getFileName)
                .collect(Collectors.toSet());

        copyMissingFiles(filesPresent, desiredFiles);
        deleteExtraFiles(filesPresent);
    }

    private static void createDownloadDirectory() {
        try {
            Files.createDirectories(DOWNLOAD_DIR);
        } catch (IOException e) {
            log.error("Could not create download directory", e);
        }
    }

    private static Set<String> getFilesPresent() {
        try (Stream<Path> paths = Files.list(DOWNLOAD_DIR)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            log.error("Error listing files in download directory", e);
            return new HashSet<>();
        }
    }

    private static Set<Sound> getDesiredSoundList() {
        return Arrays.stream(Sound.values()).collect(Collectors.toSet());
    }

    private static void copyMissingFiles(Set<String> filesPresent, Set<String> desiredFiles) {
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
    }

    private static void deleteExtraFiles(Set<String> filesPresent) {
        for (String filename : filesPresent) {
            try {
                Files.delete(DOWNLOAD_DIR.resolve(filename));
            } catch (IOException e) {
                log.warn("Failed to delete file: {}", filename, e);
            }
        }
    }

    public static InputStream getSoundStream(Sound sound) throws FileNotFoundException {
        return new FileInputStream(DOWNLOAD_DIR.resolve(sound.getFileName()).toFile());
    }
}