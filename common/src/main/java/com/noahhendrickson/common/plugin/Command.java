package com.noahhendrickson.common.plugin;

import java.util.List;

public abstract class Command<T extends Plugin<?>> implements ICommand<T> {

    private final T plugin;

    private final String name;
    private final String description;

    private final List<ISubCommand<?>> subCommands;
    private final List<ISubCommandGroup<?>> subCommandGroups;

    public Command(T plugin, String name, String description) {
        this(plugin, name, description, List.of(), List.of());
    }

    public Command(T plugin, String name, String description, List<ISubCommand<?>> subCommands, List<ISubCommandGroup<?>> subCommandGroups) {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
        this.subCommands = subCommands;
        this.subCommandGroups = subCommandGroups;
    }

    @Override
    public T getPlugin() {
        return plugin;
    }

    @Override
    public List<ISubCommand<?>> getSubCommands() {
        return subCommands;
    }

    @Override
    public List<ISubCommandGroup<?>> getSubCommandGroups() {
        return subCommandGroups;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
