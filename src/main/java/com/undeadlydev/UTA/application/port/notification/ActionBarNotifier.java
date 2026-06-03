package com.undeadlydev.UTA.application.port.notification;

import org.bukkit.entity.Player;

/** Renders action bar messages to a player. */
public interface ActionBarNotifier {

    void send(Player player, String message);

    void clear(Player player);
}
