package com.noahhendrickson.common.plugin;

public interface ISubCommand<P extends Identifiable & Executable> extends Identifiable, Executable {

    P getParent();
}
