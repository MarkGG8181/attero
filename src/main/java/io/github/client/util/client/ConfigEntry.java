package io.github.client.util.client;

import java.nio.file.attribute.FileTime;

public record ConfigEntry(String name, FileTime createdTime) {
    @Override
    public String toString() {
        return name + " (created: " + createdTime + ")";
    }
}
