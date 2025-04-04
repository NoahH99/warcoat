package com.noahhendrickson.warcoat.plugin;

import com.noahhendrickson.common.plugin.Plugin;
import com.noahhendrickson.warcoat.service.PluginManagerService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class PluginManager extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);

    private final PluginManagerService pluginManagerService;
    private final JDA jda;
    private final List<? extends Plugin<?>> plugins;

    @Autowired
    public PluginManager(PluginManagerService pluginManagerService, JDA jda, List<? extends Plugin<?>> plugins) {
        this.pluginManagerService = pluginManagerService;
        this.plugins = plugins;
        this.jda = jda;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Initializing Plugin Manager");

        // Register this class as a JDA listener to handle slash commands
        jda.addEventListener(this);

        // Register all plugins
        registerAllPlugins();

        LOGGER.info("Plugin Manager initialized with {} plugins", plugins.size());
    }

    public void registerAllPlugins() {
        LOGGER.info("Registering all plugins...");
        for (Plugin<?> plugin : plugins) registerPlugin(plugin);
        LOGGER.info("All plugins registered successfully");
    }

    public void registerPlugin(Plugin<?> plugin) {
        LOGGER.info("Registering plugin: {}", plugin.getClass().getSimpleName());
        pluginManagerService.registerPlugin(plugin);
    }

    public void unregisterPlugin(Plugin<?> plugin) {
        LOGGER.info("Unregistering plugin: {}", plugin.getClass().getSimpleName());
        pluginManagerService.unregisterPlugin(plugin);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        pluginManagerService.handleSlashCommand(event);
    }
}
