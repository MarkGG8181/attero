package fag.ware.client.tracker;

import lombok.Getter;

import java.util.HashSet;
import java.util.List;

@Getter
public abstract class AbstractTracker<T> {
    private final HashSet<T> set = new HashSet<>();

    public abstract void initialize();

    public List<T> toList() {
        return getSet().stream().toList();
    }

    public <C extends T> C getByClass(Class<C> clazz) {
        return getSet().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElse(null);
    }
}