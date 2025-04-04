package com.noahhendrickson.warcoat.service.welcome;

import com.noahhendrickson.warcoat.entity.discord.embed.DiscordEmbed;
import com.noahhendrickson.warcoat.entity.welcome.MessageType;
import com.noahhendrickson.warcoat.entity.welcome.WelcomeConfiguration;
import com.noahhendrickson.warcoat.entity.welcome.WelcomeMessage;
import com.noahhendrickson.warcoat.repository.welcome.WelcomeMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WelcomeMessageService {

    private final WelcomeMessageRepository messageRepository;

    public void sendWelcomeMessage(Guild guild, User user, WelcomeConfiguration config) {
        log.info("Sending welcome message to user {} in guild {}", user.getId(), guild.getId());
        sendMessage(guild, user, config, MessageType.WELCOME);
    }

    public void sendGoodbyeMessage(Guild guild, User user, WelcomeConfiguration config) {
        log.info("Sending goodbye message to user {} in guild {}", user.getId(), guild.getId());
        sendMessage(guild, user, config, MessageType.GOODBYE);
    }

    private void sendMessage(Guild guild, User user, WelcomeConfiguration config, MessageType type) {
        log.debug("Retrieving {} message for guild {}", type.name().toLowerCase(), guild.getId());
        Optional<WelcomeMessage> messageOpt = messageRepository.findByEnabledAndGuildIdAndMessageType(true, guild.getIdLong(), type);

        if (messageOpt.isPresent()) {
            log.debug("Found {} message for guild {}", type.name().toLowerCase(), guild.getId());
            WelcomeMessage message = messageOpt.get();

            if (config.getDm()) {
                log.debug("Sending {} message to user {} DM", type.name().toLowerCase(), user.getId());
                sendToDM(user, guild, message);
            }

            if (config.getWelcomeChannelId() != null) {
                TextChannel channel = guild.getTextChannelById(config.getWelcomeChannelId());
                if (channel != null) {
                    log.debug("Sending {} message to channel {} in guild {}", type.name().toLowerCase(), channel.getId(), guild.getId());
                    sendToChannel(guild, user, channel, message);
                } else {
                    log.warn("Welcome channel {} not found in guild {}", config.getWelcomeChannelId(), guild.getId());
                }
            }
        } else {
            log.debug("No {} message found for guild {}", type.name().toLowerCase(), guild.getId());
        }
    }

    private void sendToDM(User user, Guild guild, WelcomeMessage message) {
        log.debug("Opening private channel for user {}", user.getId());
        user.openPrivateChannel().queue(
                pc -> {
                    log.debug("Successfully opened private channel for user {}", user.getId());
                    sendMessage(guild, user, pc, message);
                },
                error -> log.error("Failed to open private channel for user {}: {}", user.getId(), error.getMessage())
        );
    }

    private void sendToChannel(Guild guild, User user, TextChannel channel, WelcomeMessage message) {
        log.debug("Sending message to channel {} in guild {}", channel.getId(), guild.getId());
        sendMessage(guild, user, channel, message);
    }

    private void sendMessage(Guild guild, User user, MessageChannel channel, WelcomeMessage message) {
        String text = message.getMessageText();
        DiscordEmbed embed = message.getEmbed();

        MessageCreateAction action = null;

        if (text != null && !text.isEmpty()) {
            action = channel.sendMessage(text);

            if (embed != null) action.addEmbeds(embed.toJdaEntity(guild, user));
        } else if (embed != null) {
            action = channel.sendMessageEmbeds(embed.toJdaEntity(guild, user));
        }

        if (action != null) action.queue();
    }
}
