package io.github.client.command;

import io.github.client.command.data.CommandInfo;
import io.github.client.util.interfaces.IMinecraft;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AbstractCommand implements IMinecraft {
    private final CommandInfo info;

    public AbstractCommand() {
        if (!getClass().isAnnotationPresent(CommandInfo.class)) {
            throw new RuntimeException("Command " + getClass().getName() + " is missing @CommandInfo");
        }

        info = getClass().getAnnotation(CommandInfo.class);
    }

    @Override
    public String toString() {
        return info.name();
    }

    public List<String> getHelp() {
        return new ArrayList<>();
    }

    public abstract void execute(String[] args);
}