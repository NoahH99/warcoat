package com.noahhendrickson.warcoat.service;

import com.noahhendrickson.common.plugin.*;
import com.noahhendrickson.warcoat.entity.plugin.PluginCommand;
import com.noahhendrickson.warcoat.repository.plugin.PluginCommandRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PluginManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginManagerService.class);

    private final PluginCommandRepository pluginCommandRepository;
    private final JDA jda;

    private final Map<String, ICommand<?>> commandMap = new HashMap<>();

    public void registerPlugin(Plugin<?> plugin) {
        LOGGER.info("Registering plugin: {}", plugin.getName());

        plugin.getCommands().forEach(this::registerCommand);
        plugin.getListeners().forEach(this::registerEvent);
    }

    public void unregisterPlugin(Plugin<?> plugin) {
        LOGGER.info("Unregistering plugin: {}", plugin.getName());

        plugin.getCommands().forEach(command -> unregisterCommand(command, plugin));
        plugin.getListeners().forEach(this::unregisterEvent);
    }

    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        try {
            String commandName = event.getName();
            String subcommandGroupName = event.getSubcommandGroup();
            String subcommandName = event.getSubcommandName();

            ICommand<?> command = commandMap.values()
                    .stream()
                    .filter(cmd -> cmd.getName().equals(commandName))
                    .findFirst()
                    .orElse(null);

            if (command == null) {
                event.reply("Unknown command.").setEphemeral(true).queue();
                return;
            }

            CommandContext ctx = new CommandContext(event);
            if (subcommandGroupName != null && subcommandName != null) {
                ISubCommandGroup<?> group = command.getSubCommandGroups()
                        .stream()
                        .filter(g -> g.getName().equals(subcommandGroupName))
                        .findFirst()
                        .orElse(null);

                if (group == null) {
                    event.reply("Unknown subcommand group.").setEphemeral(true).queue();
                    return;
                }

                ISubCommand<?> subcommand = group.getSubCommands()
                        .stream()
                        .filter(sc -> sc.getName().equals(subcommandName))
                        .findFirst()
                        .orElse(null);

                if (subcommand == null) {
                    event.reply("Unknown subcommand in group.").setEphemeral(true).queue();
                    return;
                }

                subcommand.execute(ctx);
                return;
            }

            if (subcommandName != null) {
                ISubCommand<?> subcommand = command.getSubCommands()
                        .stream()
                        .filter(sc -> sc.getName().equals(subcommandName))
                        .findFirst()
                        .orElse(null);

                if (subcommand == null) {
                    event.reply("Unknown subcommand.").setEphemeral(true).queue();
                    return;
                }

                subcommand.execute(ctx);
                return;
            }

            command.execute(ctx);
        } catch (Exception e) {
            event.reply("An error occurred while processing the command.").setEphemeral(true).queue();
            LOGGER.error("Error while handling slash command", e);
        }
    }

    private void registerCommand(ICommand<?> command) {
        String pluginName = command.getPlugin().getName();
        String commandName = command.getName();

        LOGGER.info("Registering command: {}/{}", pluginName, commandName);

        for (Guild guild : jda.getGuilds()) {
            pluginCommandRepository.findByPluginNameAndCommandNameAndGuildId(pluginName, commandName, guild.getIdLong())
                    .filter(PluginCommand::getEnabled)
                    .ifPresent(pluginCommand -> {
                        commandMap.put(pluginName, command);

                        SlashCommandData commandData = Commands.slash(command.getName(), command.getDescription());

                        command.getSubCommands().forEach(subCommand -> commandData.addSubcommands(new SubcommandData(subCommand.getName(), subCommand.getDescription())));
                        command.getSubCommandGroups().forEach(subGroup -> commandData.addSubcommandGroups(new SubcommandGroupData(subGroup.getName(), subGroup.getDescription())));

                        guild.upsertCommand(commandData).queue(
                                success -> LOGGER.info("Registered command {}/{} for guild {}", pluginName, commandName, guild.getName()),
                                error -> LOGGER.warn("Failed to register command {}/{} for guild {}: {}", pluginName, commandName, guild.getName(), error.getMessage())
                        );
                    });
        }
    }

    private void registerEvent(EventListener<?> listener) {
        String pluginName = listener.getPlugin().getName();
        String listenerName = listener.getName();

        LOGGER.info("Registering event listener: {}/{}", pluginName, listenerName);
        jda.addEventListener(listener);
    }

    private void unregisterCommand(ICommand<?> command, Plugin<?> plugin) {

    }

    private void unregisterEvent(EventListener<?> eventListener) {

    }
}
