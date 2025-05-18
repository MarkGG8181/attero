package fag.ware.client.command.impl;

import fag.ware.client.command.AbstractCommand;
import fag.ware.client.command.data.CommandInfo;

@CommandInfo(name = "GarbageCollector", description = "Runs the java garbage collector", aliases = {"gc", "garbagecollect", "gcol"})
public class GarbageCollectorCommand extends AbstractCommand
{
    @Override
    public void execute(String[] args)
    {
        System.gc();
    }
}
