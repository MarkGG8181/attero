package fag.ware.client.tracker.impl;

import fag.ware.client.file.impl.FriendsFile;
import fag.ware.client.tracker.AbstractTracker;

public class FriendTracker extends AbstractTracker<String> {
    private static final FriendTracker tracker = new FriendTracker();

    public final FriendsFile friendsFile = new FriendsFile();

    public static FriendTracker getInstance() {
        return tracker;
    }

    @Override
    public void initialize() {
        friendsFile.load();
    }
}