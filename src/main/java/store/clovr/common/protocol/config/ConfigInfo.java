package store.clovr.common.protocol.config;

public class ConfigInfo {
    public long id;
    public String name;
    public String description;
    public String uploaderUsername;
    public int upvotes;
    public int downvotes;

    public ConfigInfo(long id, String name, String description, String uploaderUsername, int upvotes, int downvotes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.uploaderUsername = uploaderUsername;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: '%s', Uploader: %s, Votes: +%d/-%d",
                id, name, uploaderUsername, upvotes, downvotes);
    }
}