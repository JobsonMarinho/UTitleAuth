package com.undeadlydev.UTA.application.usecase;

import com.google.inject.Inject;
import com.undeadlydev.UTA.application.port.AuthGateway;
import com.undeadlydev.UTA.application.port.PremiumGateway;
import com.undeadlydev.UTA.domain.model.AuthStage;
import com.undeadlydev.UTA.domain.service.AuthStageResolver;
import org.bukkit.entity.Player;

/**
 * Reaction to AuthMe's LoginEvent: stop the pending countdown (restoring XP) and confirm the login,
 * using the auto-login (premium) variant when FastLogin reports the player as premium.
 */
public class HandleLoginUseCase {

    private final AuthGateway auth;
    private final PremiumGateway premium;
    private final AuthStageResolver resolver;
    private final CountdownService countdowns;
    private final NotificationDispatcher dispatcher;

    @Inject
    public HandleLoginUseCase(AuthGateway auth,
                              PremiumGateway premium,
                              AuthStageResolver resolver,
                              CountdownService countdowns,
                              NotificationDispatcher dispatcher) {
        this.auth = auth;
        this.premium = premium;
        this.resolver = resolver;
        this.countdowns = countdowns;
        this.dispatcher = dispatcher;
    }

    public void handle(Player player) {
        if (!auth.isAuthenticated(player)) {
            return;
        }
        countdowns.stop(player);
        AuthStage stage = resolver.resolveOnLogin(premium.isPremium(player));
        dispatcher.sendCompletion(player, stage);
    }
}
