package com.noahhendrickson.common.plugin;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.List;

public interface ISubCommandGroup<P extends Identifiable & Executable> extends Identifiable {

    P getParent();

    List<ISubCommand<?>> getSubCommands();
}
