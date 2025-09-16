package io.github.client.tracker.impl;

import io.github.client.file.impl.FriendsFile;
import io.github.client.tracker.AbstractTracker;

public class FriendTracker extends AbstractTracker<String> {
    public static final FriendTracker INSTANCE = new FriendTracker();

    public final FriendsFile friendsFile = new FriendsFile();

    @Override
    public void initialize() {
        friendsFile.load();
    }
}