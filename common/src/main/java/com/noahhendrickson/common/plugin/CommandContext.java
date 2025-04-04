package com.noahhendrickson.common.plugin;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

@Getter
public class CommandContext {

    private final SlashCommandInteractionEvent event;
    private final MessageChannelUnion channel;
    private final User user;
    private final Guild guild;
    private final JDA jda;

    public CommandContext(SlashCommandInteractionEvent event) {
        this.event = event;
        this.channel = event.getChannel();
        this.user = event.getUser();
        this.guild = event.getGuild();
        this.jda = event.getJDA();
    }

    public ReplyCallbackAction reply(String content) {
        return event.reply(content);
    }
}
