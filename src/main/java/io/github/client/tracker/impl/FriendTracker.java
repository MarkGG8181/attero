package io.github.client.tracker.impl;

import io.github.client.file.impl.FriendsFile;
import io.github.client.tracker.AbstractTracker;

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