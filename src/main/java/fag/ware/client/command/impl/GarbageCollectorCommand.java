package fag.ware.client.command.impl;

import fag.ware.client.command.AbstractCommand;

public class GarbageCollectorCommand extends AbstractCommand
{
    @Override
    public void execute(String[] args)
    {
        System.gc();
    }
}
