package com.undeadlydev.UTA.application.usecase;

import com.google.inject.Inject;
import com.undeadlydev.UTA.application.port.AuthGateway;
import com.undeadlydev.UTA.application.port.MessageProvider;
import com.undeadlydev.UTA.application.port.SettingsProvider;
import com.undeadlydev.UTA.application.port.notification.MessageNotifier;
import com.undeadlydev.UTA.domain.model.AuthStage;
import com.undeadlydev.UTA.domain.service.AuthStageResolver;
import org.bukkit.entity.Player;

/**
 * Run shortly after a player joins (once AuthMe has had a chance to auto-login): decide whether the
 * player still owes a registration or a login and start the matching pending notifications + greeting.
 */
public class HandlePlayerJoinUseCase {

    private final AuthGateway auth;
    private final AuthStageResolver resolver;
    private final CountdownService countdowns;
    private final SettingsProvider settings;
    private final MessageProvider messages;
    private final MessageNotifier chat;

    @Inject
    public HandlePlayerJoinUseCase(AuthGateway auth,
                                   AuthStageResolver resolver,
                                   CountdownService countdowns,
                                   SettingsProvider settings,
                                   MessageProvider messages,
                                   MessageNotifier chat) {
        this.auth = auth;
        this.resolver = resolver;
        this.countdowns = countdowns;
        this.settings = settings;
        this.messages = messages;
        this.chat = chat;
    }

    public void handle(Player player) {
        if (!player.isOnline() || auth.isAuthenticated(player)) {
            return;
        }
        resolver.resolveOnJoin(auth.isRegistered(player), false).ifPresent(stage -> {
            countdowns.startPending(player, stage);
            if (settings.isJoinWelcomeEnabled(stage)) {
                String key = stage == AuthStage.NO_REGISTER
                        ? "message.welcome.onjoin.register"
                        : "message.welcome.onjoin.login";
                chat.send(player, messages.get(player, key), settings.isJoinWelcomeCentered(stage));
            }
            countdowns.startXp(player);
        });
    }
}
