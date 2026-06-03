package com.undeadlydev.UTA.application.usecase;

import com.google.inject.Inject;
import com.undeadlydev.UTA.domain.model.AuthStage;
import org.bukkit.entity.Player;

/** Reaction to AuthMe's unregister events (by player or by admin): the player is pending registration. */
public class HandleUnregisterUseCase {

    private final CountdownService countdowns;

    @Inject
    public HandleUnregisterUseCase(CountdownService countdowns) {
        this.countdowns = countdowns;
    }

    public void handle(Player player) {
        countdowns.startPending(player, AuthStage.NO_REGISTER);
    }
}
