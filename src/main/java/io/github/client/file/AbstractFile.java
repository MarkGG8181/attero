package io.github.client.file;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.client.Attero;
import io.github.client.util.java.FileUtil;
import lombok.Getter;

import java.io.*;

/**
 * @author markuss
 * @since 22/05/2025
 */
@Getter
public abstract class AbstractFile {
    private final File file;
    private final String fileName;

    public AbstractFile(String fileName) {
        fileName += (fileName.endsWith(".json") ? "" : ".json");

        this.file = FileUtil.CLIENT_DIR.resolve(File.separator + fileName).toFile();
        this.fileName = fileName;
    }

    public abstract void save();

    public abstract void load();

    public void saveJsonObject(JsonObject json) {
        try (Writer writer = new FileWriter(file)) {
            FileUtil.GSON.toJson(json, writer);
        } catch (IOException e) {
            Attero.LOGGER.error("Failed to save {}", fileName, e);
        }
    }

    public void saveJsonElement(JsonElement json) {
        try (Writer writer = new FileWriter(file)) {
            FileUtil.GSON.toJson(json, writer);
        } catch (IOException e) {
            Attero.LOGGER.error("Failed to save {}", fileName, e);
        }
    }

    public JsonArray loadJsonArray() {
        if (!file.exists()) return new JsonArray();
        try (Reader reader = new FileReader(file)) {
            return JsonParser.parseReader(reader).getAsJsonArray();
        } catch (IOException | IllegalStateException e) {
            Attero.LOGGER.error("Failed to load {}", fileName, e);
            return new JsonArray();
        }
    }

    public JsonObject loadJsonObject() {
        if (!file.exists()) return new JsonObject();
        try (Reader reader = new FileReader(file)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            Attero.LOGGER.error("Failed to load {}", fileName, e);
            return null;
        }
    }
}