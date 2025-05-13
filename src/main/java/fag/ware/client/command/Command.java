package fag.ware.client.command;

import fag.ware.client.command.data.CommandInfo;
import fag.ware.client.util.IMinecraft;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Command implements IMinecraft {
    private final CommandInfo info;

    public Command() {
        if (!getClass().isAnnotationPresent(CommandInfo.class)) {
            throw new RuntimeException("Command " + getClass().getName() + " is missing @CommandInfo");
        }

        info = getClass().getAnnotation(CommandInfo.class);
    }

    @Override
    public String toString() {
        return info.name();
    }

    public abstract void execute(String[] args);
}