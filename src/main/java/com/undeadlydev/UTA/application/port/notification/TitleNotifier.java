package com.undeadlydev.UTA.application.port.notification;

import org.bukkit.entity.Player;

/** Renders titles/subtitles to a player. */
public interface TitleNotifier {

    void send(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);

    void clear(Player player);
}
