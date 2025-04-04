package com.noahhendrickson.warcoat.plugin.welcome.command;

import com.noahhendrickson.common.plugin.CommandContext;
import com.noahhendrickson.common.plugin.SubCommand;
import org.springframework.stereotype.Component;

@Component
public class TestSubCommand extends SubCommand<TestParentCommand> {

    public TestSubCommand(TestParentCommand parent) {
        super("sub-1", "Sub command of " + parent.getName(), parent);
    }

    @Override
    public void execute(CommandContext ctx) {
        ctx.reply("Sub command executed").queue();
    }
}
