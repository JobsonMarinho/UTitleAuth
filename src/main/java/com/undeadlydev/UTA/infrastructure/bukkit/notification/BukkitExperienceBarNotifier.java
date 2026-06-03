package com.undeadlydev.UTA.infrastructure.bukkit.notification;

import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.notification.ExperienceBarNotifier;
import org.bukkit.entity.Player;

/** {@link ExperienceBarNotifier} driving the vanilla XP bar via the Bukkit Player API. */
@Singleton
public class BukkitExperienceBarNotifier implements ExperienceBarNotifier {

    @Override
    public int currentLevel(Player player) {
        return player.getLevel();
    }

    @Override
    public float currentExp(Player player) {
        return player.getExp();
    }

    @Override
    public void setLevel(Player player, int level) {
        player.setLevel(level);
    }

    @Override
    public void setExp(Player player, float exp) {
        player.setExp(exp);
    }
}
