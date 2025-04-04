package com.noahhendrickson.warcoat.plugin.welcome.listener;

import com.noahhendrickson.common.plugin.EventListener;
import com.noahhendrickson.warcoat.plugin.welcome.WelcomePlugin;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class WelcomeListener extends EventListener<WelcomePlugin> {

    @Autowired
    public WelcomeListener(@Lazy WelcomePlugin welcomePlugin) {
        super(welcomePlugin);
    }

    @Override
    public String getName() {
        return "Welcome Listener";
    }

    @Override
    public String getDescription() {
        return "Listens for member join events and member remove events";
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        super.getPlugin().handleMemberJoin(event.getGuild(), event.getUser());
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        super.getPlugin().handleMemberRemove(event.getGuild(), event.getUser());
    }
}
