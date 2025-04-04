package com.noahhendrickson.warcoat.plugin.welcome.command;

import com.noahhendrickson.common.plugin.Command;
import com.noahhendrickson.common.plugin.CommandContext;
import com.noahhendrickson.warcoat.plugin.welcome.WelcomePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class TestParentCommand extends Command<WelcomePlugin> {

    @Autowired
    public TestParentCommand(@Lazy WelcomePlugin plugin) {
        super(plugin, "parent", "Example parent command");
    }

    @Override
    public void execute(CommandContext ctx) {
        ctx.reply("Parent Command").queue();
    }
}
