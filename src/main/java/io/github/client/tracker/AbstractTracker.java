package io.github.client.tracker;

import io.github.client.Attero;

import java.util.ArrayList;
import java.util.List;

/**
 * @author markuss
 * @since 4/05/2025
 */
public abstract class AbstractTracker<T> {
    public final List<T> list = new ArrayList<>();

    public void initialize() {
        Attero.BUS.register(this);
    }

    public List<T> toList() {
        return list.stream().toList();
    }

    public <C extends T> C getByClass(Class<C> clazz) {
        return list.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElse(null);
    }
}