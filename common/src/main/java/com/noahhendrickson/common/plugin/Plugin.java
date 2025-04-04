package com.noahhendrickson.common.plugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public interface Plugin<T extends Plugin<?>> {

    String getName();

    String getDescription();

    List<EventListener<T>> getListeners();

    List<ICommand<T>> getCommands();

    boolean isEnabled(Guild guild);
}
