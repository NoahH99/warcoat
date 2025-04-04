package com.noahhendrickson.common.plugin;

import lombok.Getter;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Getter
public abstract class EventListener<T extends Plugin<?>> extends ListenerAdapter implements Identifiable {

    private final T plugin;

    public EventListener(T plugin) {
        this.plugin = plugin;
    }
}
