package com.undeadlydev.UTA.infrastructure.bukkit.listener;

import com.google.inject.Inject;
import com.undeadlydev.UTA.application.usecase.HandleUnregisterUseCase;
import fr.xephi.authme.events.UnregisterByAdminEvent;
import fr.xephi.authme.events.UnregisterByPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/** Handles both player-initiated and admin-initiated unregistration with a single use case. */
public class AuthMeUnregisterListener implements Listener {

    private final HandleUnregisterUseCase useCase;

    @Inject
    public AuthMeUnregisterListener(HandleUnregisterUseCase useCase) {
        this.useCase = useCase;
    }

    @EventHandler
    public void onUnregisterByPlayer(UnregisterByPlayerEvent event) {
        dispatch(event.getPlayer());
    }

    @EventHandler
    public void onUnregisterByAdmin(UnregisterByAdminEvent event) {
        dispatch(event.getPlayer());
    }

    private void dispatch(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        useCase.handle(player);
    }
}
