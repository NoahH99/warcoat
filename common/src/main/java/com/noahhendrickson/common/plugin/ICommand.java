package com.noahhendrickson.common.plugin;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.List;

public interface ICommand<T extends Plugin<?>> extends Identifiable, Executable {

    T getPlugin();

    List<ISubCommand<?>> getSubCommands();

    List<ISubCommandGroup<?>> getSubCommandGroups();
}