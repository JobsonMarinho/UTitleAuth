package com.undeadlydev.UTA.application.port.notification;

import org.bukkit.entity.Player;

/** Manages a per-player boss bar. {@link #show} creates-or-updates it; colors/styles are raw config names. */
public interface BossBarNotifier {

    void show(Player player, String message, String color, String style);

    void remove(Player player);
}
