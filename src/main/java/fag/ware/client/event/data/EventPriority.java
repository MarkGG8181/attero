package fag.ware.client.event.data;

public enum EventPriority {
    HIGHEST(5),
    HIGH(4),
    NORMAL(3),
    LOW(2),
    LOWEST(1);

    public final int level;

    EventPriority(int level) {
        this.level = level;
    }
}