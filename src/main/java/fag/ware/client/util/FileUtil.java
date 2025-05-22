package fag.ware.client.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fag.ware.client.Fagware;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("ALL")
public class FileUtil {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public record ConfigEntry(String name, FileTime createdTime) {
    }

    public static void createDir(String path) {
        try {
            Files.createDirectories(Path.of(path)); // doesn't throw if already exists
        } catch (IOException e) {
            Fagware.LOGGER.error("Failed to create directory {}", path, e);
        }
    }

    public static Set<ConfigEntry> listConfigFiles() {
        return listFiles(Fagware.MOD_ID + File.separator + "configs", ".json");
    }

    public static Set<ConfigEntry> listFiles(String dir, String fileSuffix) {
        File folder = new File(dir);
        File[] files = folder.listFiles();
        if (files == null) return Collections.emptySet();

        return Stream.of(files)
                .filter(file -> !file.isDirectory() && file.getName().endsWith(fileSuffix))
                .map(file -> {
                    try {
                        Path path = file.toPath();
                        FileTime creationTime = Files.readAttributes(path, BasicFileAttributes.class).creationTime();
                        return new ConfigEntry(file.getName(), creationTime);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return new ConfigEntry(file.getName(), FileTime.fromMillis(0)); // fallback
                    }
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
