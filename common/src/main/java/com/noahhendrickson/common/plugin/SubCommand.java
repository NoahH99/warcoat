package com.noahhendrickson.common.plugin;

public abstract class SubCommand<P extends Identifiable & Executable> implements ISubCommand<P> {

    private final String name;
    private final String description;

    private final P parent;

    public SubCommand(String name, String description, P parent) {
        this.name = name;
        this.description = description;
        this.parent = parent;
    }

    @Override
    public P getParent() {
        return parent;
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
