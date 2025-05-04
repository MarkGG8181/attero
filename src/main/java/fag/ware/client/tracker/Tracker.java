package fag.ware.client.tracker;

import java.util.HashSet;
import java.util.List;

public abstract class Tracker<T> {
    private final HashSet<T> set = new HashSet<>();

    public abstract void initialize();

    public HashSet<T> getSet() {
        return set;
    }

    public List<T> toList() {
        return getSet().stream().toList();
    }
}
