package com.undeadlydev.UTA.application.usecase;

import com.google.inject.Inject;
import com.undeadlydev.UTA.application.port.SettingsProvider;
import com.undeadlydev.UTA.domain.model.AuthStage;
import com.undeadlydev.UTA.domain.service.AuthStageResolver;
import org.bukkit.entity.Player;

/**
 * Reaction to AuthMe's RegisterEvent. With {@code forceLoginAfterRegister} the player is still
 * pending login (NO_LOGIN); otherwise the registration is confirmed.
 */
public class HandleRegisterUseCase {

    private final AuthStageResolver resolver;
    private final SettingsProvider settings;
    private final CountdownService countdowns;
    private final NotificationDispatcher dispatcher;

    @Inject
    public HandleRegisterUseCase(AuthStageResolver resolver,
                                 SettingsProvider settings,
                                 CountdownService countdowns,
                                 NotificationDispatcher dispatcher) {
        this.resolver = resolver;
        this.settings = settings;
        this.countdowns = countdowns;
        this.dispatcher = dispatcher;
    }

    public void handle(Player player) {
        AuthStage stage = resolver.resolveOnRegister(settings.forceLoginAfterRegister());
        if (stage.isPending()) {
            // forceLoginAfterRegister: keep the running XP countdown, restart the pending title/bars as NO_LOGIN.
            countdowns.startPending(player, stage);
            return;
        }
        countdowns.stop(player);
        dispatcher.sendCompletion(player, stage);
    }
}
