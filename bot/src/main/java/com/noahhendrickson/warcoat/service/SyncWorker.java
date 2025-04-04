package com.noahhendrickson.warcoat.service;

import com.noahhendrickson.common.plugin.ICommand;
import com.noahhendrickson.common.plugin.Plugin;
import com.noahhendrickson.warcoat.entity.discord.Guild;
import com.noahhendrickson.warcoat.entity.plugin.PluginCommand;
import com.noahhendrickson.warcoat.repository.discord.GuildRepository;
import com.noahhendrickson.warcoat.repository.plugin.PluginCommandRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SyncWorker {

    private final PluginCommandRepository pluginCommandRepository;
    private final GuildRepository guildRepository;
    private final List<? extends Plugin<?>> plugins;
    private final JDA jda;

    @Autowired
    public SyncWorker(
            PluginCommandRepository pluginCommandRepository,
            GuildRepository guildRepository,
            List<? extends Plugin<?>> plugins,
            JDA jda) {
        this.pluginCommandRepository = pluginCommandRepository;
        this.guildRepository = guildRepository;
        this.plugins = plugins;
        this.jda = jda;
    }

    /**
     * Synchronizes commands between plugins and the database
     * - Adds missing commands to the database
     * - Removes commands from the database that no longer exist in code
     * - Preserves enabled status for existing commands
     */
    @Transactional
    public void syncCommands() {
        log.info("Starting command synchronization");

        // Process each guild separately
        for (net.dv8tion.jda.api.entities.Guild jdaGuild : jda.getGuilds()) {
            Long guildId = jdaGuild.getIdLong();
            log.info("Syncing commands for guild: {} (ID: {})", jdaGuild.getName(), guildId);

            // Find or create Guild entity
            Guild guild = guildRepository.findById(guildId)
                    .orElseGet(() -> createGuildEntity(jdaGuild));

            // Get all commands in the database for this guild
            List<PluginCommand> existingCommands = pluginCommandRepository.findByGuildId(guildId);

            // Create a map of existing commands for faster lookup
            Map<String, PluginCommand> existingCommandMap = existingCommands.stream()
                    .collect(Collectors.toMap(
                            cmd -> createCommandKey(cmd.getPluginName(), cmd.getCommandName()),
                            cmd -> cmd
                    ));

            // Track commands that should exist in the database
            Set<String> validCommandKeys = new HashSet<>();

            // Process all plugins and their commands
            for (Plugin<?> plugin : plugins) {
                String pluginName = plugin.getName();

                for (ICommand<?> command : plugin.getCommands()) {
                    String commandName = command.getName();
                    String commandKey = createCommandKey(pluginName, commandName);
                    validCommandKeys.add(commandKey);

                    // Check if command already exists in database
                    if (!existingCommandMap.containsKey(commandKey)) {
                        // Command doesn't exist in database, add it
                        createCommand(plugin, command, guild);
                    }
                }
            }

            // Remove commands that no longer exist in the code
            List<PluginCommand> commandsToRemove = existingCommands.stream()
                    .filter(cmd -> !validCommandKeys.contains(createCommandKey(cmd.getPluginName(), cmd.getCommandName())))
                    .toList();

            if (!commandsToRemove.isEmpty()) {
                log.info("Removing {} obsolete commands for guild {}", commandsToRemove.size(), guild.getName());
                pluginCommandRepository.deleteAll(commandsToRemove);
            }
        }

        log.info("Command synchronization completed");
    }

    private Guild createGuildEntity(net.dv8tion.jda.api.entities.Guild jdaGuild) {
        log.info("Creating new Guild entity for: {} (ID: {})", jdaGuild.getName(), jdaGuild.getIdLong());

        Guild guild = Guild.builder()
                .id(jdaGuild.getIdLong())
                .name(jdaGuild.getName())
                .iconUrl(jdaGuild.getIconUrl())
                .ownerId(jdaGuild.getOwnerIdLong())
                .createdAt(LocalDateTime.now()) // This is not accurate but JDA doesn't provide creation time
                .memberCount(jdaGuild.getMemberCount())
                .premiumTier((short) jdaGuild.getBoostTier().getKey())
                .description(jdaGuild.getDescription())
                .bannerUrl(jdaGuild.getBannerUrl())
                .systemChannelId(jdaGuild.getSystemChannel() != null ? jdaGuild.getSystemChannel().getIdLong() : null)
                .rulesChannelId(jdaGuild.getRulesChannel() != null ? jdaGuild.getRulesChannel().getIdLong() : null)
                .publicUpdatesChannelId(jdaGuild.getCommunityUpdatesChannel() != null ?
                        jdaGuild.getCommunityUpdatesChannel().getIdLong() : null)
                .isLarge(false) // Not provided by JDA
                .isUnavailable(false) // If we can access it, it's not unavailable
                .joinedAt(LocalDateTime.now()) // This is not accurate but JDA doesn't provide join time
                .build();

        return guildRepository.save(guild);
    }

    private void createCommand(Plugin<?> plugin, ICommand<?> command, Guild guild) {
        log.info("Creating command in database: {}/{} for guild {}", plugin.getName(), command.getName(), guild.getName());

        PluginCommand pluginCommand = PluginCommand.builder()
                .pluginName(plugin.getName())
                .commandName(command.getName())
                .guild(guild)
                .enabled(true) // New commands are enabled by default
                .build();

        pluginCommandRepository.save(pluginCommand);
    }

    private String createCommandKey(String pluginName, String commandName) {
        return pluginName + ":" + commandName;
    }
}
