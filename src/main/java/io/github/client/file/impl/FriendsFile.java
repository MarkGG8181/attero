package io.github.client.file.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.github.client.file.AbstractFile;
import io.github.client.tracker.impl.FriendTracker;

/**
 * @author markuss
 * @since 23/05/2025
 */
public class FriendsFile extends AbstractFile {
    public FriendsFile() {
        super("friends.json");
    }

    @Override
    public void save() {
        JsonArray jsonArray = new JsonArray();

        for (String friend : FriendTracker.INSTANCE.list) {
            jsonArray.add(friend);
        }

        saveJsonElement(jsonArray);
    }

    @Override
    public void load() {
        JsonArray jsonArray = loadJsonArray();

        FriendTracker.INSTANCE.list.clear();

        for (JsonElement element : jsonArray) {
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                FriendTracker.INSTANCE.list.add(element.getAsString());
            }
        }
    }
}