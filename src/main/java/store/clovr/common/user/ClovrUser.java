package store.clovr.common.user;

public class ClovrUser {
    private long id;
    private long loggedInProductId;
    private String loggedInProductName;
    private String username;
    private String minecraftUsername;
    private boolean isDeveloper;

    public ClovrUser() {}

    public ClovrUser(long id, String username, String minecraftUsername, boolean isDeveloper, String loggedInProductName, long loggedInProductId) {
        this.id = id;
        this.username = username;
        this.minecraftUsername = minecraftUsername;
        this.isDeveloper = isDeveloper;
        this.loggedInProductName = loggedInProductName;
        this.loggedInProductId = loggedInProductId;
    }

    public long getId() { return id; }
    public String getUsername() { return username; }
    public String getMinecraftUsername() { return minecraftUsername; }
    public boolean isDeveloper() { return isDeveloper; }
    public void setMinecraftUsername(String minecraftUsername) { this.minecraftUsername = minecraftUsername; }
    public long getLoggedInProductId() { return loggedInProductId; }
    public void setLoggedInProductId(long loggedInProductId) { this.loggedInProductId = loggedInProductId; }
    public String getLoggedInProductName() { return loggedInProductName; }
    public void setLoggedInProductName(String loggedInProductName) { this.loggedInProductName = loggedInProductName; }
}