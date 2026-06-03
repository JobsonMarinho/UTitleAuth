package com.undeadlydev.UTA.application.usecase;

import com.google.inject.Inject;
import com.undeadlydev.UTA.application.port.SessionRepository;
import org.bukkit.entity.Player;

/** Reaction to quit/kick: cancel every task, drop the boss bar and forget the player's session. */
public class PlayerCleanupUseCase {

    private final CountdownService countdowns;
    private final SessionRepository sessions;

    @Inject
    public PlayerCleanupUseCase(CountdownService countdowns, SessionRepository sessions) {
        this.countdowns = countdowns;
        this.sessions = sessions;
    }

    public void handle(Player player) {
        countdowns.stop(player);
        sessions.clear(player.getUniqueId());
    }
}
