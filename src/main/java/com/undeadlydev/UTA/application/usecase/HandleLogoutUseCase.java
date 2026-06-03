package com.undeadlydev.UTA.application.usecase;

import com.google.inject.Inject;
import com.undeadlydev.UTA.domain.model.AuthStage;
import org.bukkit.entity.Player;

/** Reaction to AuthMe's LogoutEvent: the player is pending login again. */
public class HandleLogoutUseCase {

    private final CountdownService countdowns;

    @Inject
    public HandleLogoutUseCase(CountdownService countdowns) {
        this.countdowns = countdowns;
    }

    public void handle(Player player) {
        countdowns.startPending(player, AuthStage.NO_LOGIN);
    }
}
