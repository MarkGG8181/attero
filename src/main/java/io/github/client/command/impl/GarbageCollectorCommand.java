package io.github.client.command.impl;

import io.github.client.command.AbstractCommand;
import io.github.client.command.data.CommandInfo;

@CommandInfo(name = "GarbageCollector", description = "Runs the java garbage collector", aliases = {"gc", "garbagecollect", "gcol"})
public class GarbageCollectorCommand extends AbstractCommand {
    @Override
    public void execute(String[] args) {
        System.gc();
    }
}