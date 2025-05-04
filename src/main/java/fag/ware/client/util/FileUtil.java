package fag.ware.client.util;

import fag.ware.client.Fagware;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static void createDir(String path) {
        try {
            Files.createDirectories(Path.of(path)); // doesn't throw if already exists
        } catch (IOException e) {
            Fagware.LOGGER.error("Failed to create directory {}", path, e);
        }
    }
}
