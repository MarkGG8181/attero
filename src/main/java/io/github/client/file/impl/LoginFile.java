package io.github.client.file.impl;

import com.google.gson.JsonObject;
import io.github.client.file.AbstractFile;
import io.github.client.util.client.EncryptUtil;

public class LoginFile extends AbstractFile {
    public String username, password;

    public LoginFile(String username, String password) {
        super("login.json");
        this.username = username;
        this.password = password;
    }

    @Override
    public void save() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Username", EncryptUtil.encrypt(username, "niggabuttdigger30"));
        jsonObject.addProperty("Password", EncryptUtil.encrypt(password, "niggabuttdigger31"));

        super.saveJsonObject(jsonObject);
    }

    @Override
    public void load() {
        JsonObject json = loadJsonObject();

        if (!json.isEmpty()) {
            username = EncryptUtil.decrypt(json.get("Username").getAsString(), "niggabuttdigger30");
            password = EncryptUtil.decrypt(json.get("Password").getAsString(), "niggabuttdigger31");
        }
    }
}
