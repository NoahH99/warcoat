package com.noahhendrickson.common.plugin;

import java.util.List;

public interface ISubCommandGroup<P extends Identifiable & Executable> extends Identifiable {

    P getParent();

    List<ISubCommand<?>> getSubCommands();
}
