package io.github.client.tracker.impl;

import io.github.client.tracker.AbstractTracker;
import io.github.client.util.client.ytdlp.YtDlpExecUtil;

public class MusicTracker extends AbstractTracker {
    public static final MusicTracker INSTANCE = new MusicTracker();

    @Override
    public void initialize() {
        YtDlpExecUtil.download();
    }
}
