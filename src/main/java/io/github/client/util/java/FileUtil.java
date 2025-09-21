package io.github.client.util.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.client.Attero;
import io.github.client.util.client.ConfigEntry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("ALL")
public class FileUtil {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Path CLIENT_DIR = Path.of(System.getProperty("user.home") + File.separator + Attero.MOD_ID);

    public static void createDir(String path) {
        try {
            Files.createDirectories(CLIENT_DIR.resolve(path)); // doesn't throw if already exists
        } catch (IOException e) {
            Attero.LOGGER.error("Failed to create directory {}", path, e);
        }
    }

    public static Set<ConfigEntry> listConfigFiles() {
        return listFiles(File.separator + "configs", ".json");
    }

    public static Set<ConfigEntry> listFiles(String dir, String fileSuffix) {
        File folder = CLIENT_DIR.resolve(dir).toFile();
        File[] files = folder.listFiles();
        if (files == null) return Collections.emptySet();

        return Stream.of(files)
                .filter(file -> !file.isDirectory() && file.getName().endsWith(fileSuffix))
                .map(file -> {
                    Path path = file.toPath();
                    return new ConfigEntry(file.getName(), 0);
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static String[] readAllLines(Path filePath) {
        try {
            List<String> lines = Files.readAllLines(filePath);
            return lines.toArray(new String[0]);
        } catch (IOException e) {
            Attero.LOGGER.error("Failed to read lines of {}", filePath.getFileName(), e);
        }

        return new String[]{}; //just so there's no nullptr
    }

    public static String getSHA256(Path filePath) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] fileBytes = Files.readAllBytes(filePath);
            byte[] hashBytes = digest.digest(fileBytes);

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (Exception e) {
            return "-1";
        }
    }
}