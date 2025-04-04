package com.noahhendrickson.common.plugin;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public interface ISubCommand<P extends Identifiable & Executable> extends Identifiable, Executable {

    P getParent();
}
