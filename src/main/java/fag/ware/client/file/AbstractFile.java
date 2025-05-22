package fag.ware.client.file;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fag.ware.client.Fagware;
import fag.ware.client.util.FileUtil;
import lombok.Getter;

import java.io.*;

@Getter
public abstract class AbstractFile {
    private final File file;
    private final String fileName;

    public AbstractFile(String fileName) {
        fileName += (fileName.endsWith(".json") ? "" : ".json");

        this.file = new File(Fagware.MOD_ID + File.separator + fileName);
        this.fileName = fileName;
    }

    public abstract void save();

    public abstract void load();

    public void saveJsonObject(JsonObject json) {
        try (Writer writer = new FileWriter(file)) {
            FileUtil.GSON.toJson(json, writer);
        } catch (IOException e) {
            Fagware.LOGGER.error("Failed to save {}", fileName, e);
        }
    }

    public JsonObject loadJsonObject() {
        if (!file.exists()) return new JsonObject();
        try (Reader reader = new FileReader(file)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            Fagware.LOGGER.error("Failed to load {}", fileName, e);
            return new JsonObject();
        }
    }
}