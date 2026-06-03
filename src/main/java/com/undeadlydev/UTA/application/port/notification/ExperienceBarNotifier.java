package com.undeadlydev.UTA.application.port.notification;

import org.bukkit.entity.Player;

/** Reads/writes the player's XP bar, used to drive the join countdown animation. */
public interface ExperienceBarNotifier {

    int currentLevel(Player player);

    float currentExp(Player player);

    void setLevel(Player player, int level);

    void setExp(Player player, float exp);
}
