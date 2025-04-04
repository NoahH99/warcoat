package com.noahhendrickson.common.plugin;

import java.util.List;

public interface ICommand<T extends Plugin<?>> extends Identifiable, Executable {

    T getPlugin();

    List<ISubCommand<?>> getSubCommands();

    List<ISubCommandGroup<?>> getSubCommandGroups();
}