package com.noahhendrickson.warcoat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CommandSyncService {

    private final SyncWorker syncWorker;

    @Autowired
    public CommandSyncService(SyncWorker syncWorker) {
        this.syncWorker = syncWorker;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onAppReady() {
        syncWorker.syncCommands();
    }
}
