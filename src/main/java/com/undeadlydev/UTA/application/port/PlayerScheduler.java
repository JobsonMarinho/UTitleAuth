package com.undeadlydev.UTA.application.port;

import org.bukkit.entity.Player;

/** Schedules work tied to a player/entity, abstracting Folia/Paper/Spigot differences. */
public interface PlayerScheduler {

    /** Runs {@code task} repeatedly on the player's region/thread. Delays are in ticks. */
    TaskHandle runTimer(Player player, Runnable task, long delayTicks, long periodTicks);

    /** Runs {@code task} once after {@code delayTicks} ticks on the player's region/thread. */
    TaskHandle runLater(Player player, Runnable task, long delayTicks);
}
