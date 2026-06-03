package com.undeadlydev.UTA.infrastructure.bukkit.listener;

import com.google.inject.Inject;
import com.undeadlydev.UTA.application.usecase.PlayerCleanupUseCase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/** Cleans up a player's session on quit/kick (cancels tasks, removes the boss bar, restores XP). */
public class PlayerCleanupListener implements Listener {

    private final PlayerCleanupUseCase useCase;

    @Inject
    public PlayerCleanupListener(PlayerCleanupUseCase useCase) {
        this.useCase = useCase;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        useCase.handle(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKick(PlayerKickEvent event) {
        useCase.handle(event.getPlayer());
    }
}
