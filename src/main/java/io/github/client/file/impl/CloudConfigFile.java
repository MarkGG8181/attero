package io.github.client.file.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class CloudConfigFile extends ModulesFile {
    private final String data;

    public CloudConfigFile(String configName, String data) {
        super(configName);
        this.data = data;
    }

    @Override
    public JsonObject loadJsonObject() {
        return JsonParser.parseString(data).getAsJsonObject();
    }
}