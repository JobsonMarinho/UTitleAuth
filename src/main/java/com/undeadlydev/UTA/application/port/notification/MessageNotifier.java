package com.undeadlydev.UTA.application.port.notification;

import org.bukkit.entity.Player;

/** Sends chat messages, handling multi-line text and optional centering. */
public interface MessageNotifier {

    void send(Player player, String message, boolean centered);
}
