package fag.ware.client.tracker;

import lombok.Getter;

import java.util.HashSet;
import java.util.List;

@Getter
public abstract class Tracker<T> {
    private final HashSet<T> set = new HashSet<>();

    public abstract void initialize();

    public List<T> toList() {
        return getSet().stream().toList();
    }

    public T getByClass(Class<?> clazz) {
        return getSet().stream()
                .filter(t -> t.getClass().equals(clazz))
                .findFirst()
                .orElse(null);
    }
}