package com.undeadlydev.UTA.infrastructure.bukkit.listener;

import com.google.inject.Inject;
import com.undeadlydev.UTA.application.port.PlayerScheduler;
import com.undeadlydev.UTA.application.usecase.HandlePlayerJoinUseCase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/** Bridges {@link PlayerJoinEvent} to {@link HandlePlayerJoinUseCase} after a short delay (lets AuthMe auto-login first). */
public class PlayerJoinListener implements Listener {

    private static final long JOIN_DELAY_TICKS = 20L;

    private final PlayerScheduler scheduler;
    private final HandlePlayerJoinUseCase useCase;

    @Inject
    public PlayerJoinListener(PlayerScheduler scheduler, HandlePlayerJoinUseCase useCase) {
        this.scheduler = scheduler;
        this.useCase = useCase;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        scheduler.runLater(player, () -> useCase.handle(player), JOIN_DELAY_TICKS);
    }
}
