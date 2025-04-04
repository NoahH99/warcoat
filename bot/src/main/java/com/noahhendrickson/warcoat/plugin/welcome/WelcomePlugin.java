package com.noahhendrickson.warcoat.plugin.welcome;

import com.noahhendrickson.common.plugin.EventListener;
import com.noahhendrickson.common.plugin.ICommand;
import com.noahhendrickson.common.plugin.Plugin;
import com.noahhendrickson.warcoat.service.welcome.WelcomeService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WelcomePlugin implements Plugin<WelcomePlugin> {

    private final List<EventListener<WelcomePlugin>> listeners;
    private final List<ICommand<WelcomePlugin>> commands;

    private final WelcomeService welcomeService;

    @Autowired
    public WelcomePlugin(List<EventListener<WelcomePlugin>> listeners, List<ICommand<WelcomePlugin>> commands, WelcomeService welcomeService) {
        this.listeners = listeners;
        this.commands = commands;
        this.welcomeService = welcomeService;
    }

    public void handleMemberJoin(Guild guild, User user) {
        welcomeService.handleMemberJoin(guild, user);
    }

    public void handleMemberRemove(Guild guild, User user) {
        welcomeService.handleMemberRemove(guild, user);
    }

    @Override
    public String getName() {
        return "Welcome & Goodbye";
    }

    @Override
    public String getDescription() {
        return "Say hello to new users and say goodbye to all that leave.";
    }

    @Override
    public List<EventListener<WelcomePlugin>> getListeners() {
        return listeners;
    }

    @Override
    public List<ICommand<WelcomePlugin>> getCommands() {
        return commands;
    }

    @Override
    public boolean isEnabled(Guild guild) {
        return welcomeService.isEnabled(guild);
    }
}
