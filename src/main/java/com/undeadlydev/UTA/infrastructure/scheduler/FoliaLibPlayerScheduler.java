package com.undeadlydev.UTA.infrastructure.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tcoded.folialib.impl.PlatformScheduler;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import com.undeadlydev.UTA.application.port.PlayerScheduler;
import com.undeadlydev.UTA.application.port.TaskHandle;
import org.bukkit.entity.Player;

/** {@link PlayerScheduler} backed by FoliaLib, so the plugin runs on Folia, Paper and Spigot alike. */
@Singleton
public class FoliaLibPlayerScheduler implements PlayerScheduler {

    private final PlatformScheduler scheduler;

    @Inject
    public FoliaLibPlayerScheduler(PlatformScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public TaskHandle runTimer(Player player, Runnable task, long delayTicks, long periodTicks) {
        WrappedTask wrapped = scheduler.runAtEntityTimer(player, task, delayTicks, periodTicks);
        return wrapped::cancel;
    }

    @Override
    public TaskHandle runLater(Player player, Runnable task, long delayTicks) {
        WrappedTask wrapped = scheduler.runAtEntityLater(player, task, delayTicks);
        return wrapped::cancel;
    }
}
