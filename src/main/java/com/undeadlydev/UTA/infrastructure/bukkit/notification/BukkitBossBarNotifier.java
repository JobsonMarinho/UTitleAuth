package com.undeadlydev.UTA.infrastructure.bukkit.notification;

import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.notification.BossBarNotifier;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** {@link BossBarNotifier} using the modern Bukkit boss bar API (one bar per player). */
@Singleton
public class BukkitBossBarNotifier implements BossBarNotifier {

    private final Map<UUID, BossBar> bars = new ConcurrentHashMap<>();

    @Override
    public void show(Player player, String message, String color, String style) {
        BossBar bar = bars.computeIfAbsent(player.getUniqueId(), id -> {
            BossBar created = Bukkit.createBossBar(message, parseColor(color), parseStyle(style));
            created.setProgress(1.0);
            created.addPlayer(player);
            return created;
        });
        bar.setTitle(message);
    }

    @Override
    public void remove(Player player) {
        BossBar bar = bars.remove(player.getUniqueId());
        if (bar != null) {
            bar.removePlayer(player);
        }
    }

    private BarColor parseColor(String color) {
        try {
            return BarColor.valueOf(color);
        } catch (IllegalArgumentException | NullPointerException e) {
            return BarColor.WHITE;
        }
    }

    private BarStyle parseStyle(String style) {
        try {
            return BarStyle.valueOf(style);
        } catch (IllegalArgumentException | NullPointerException e) {
            return BarStyle.SOLID;
        }
    }
}
