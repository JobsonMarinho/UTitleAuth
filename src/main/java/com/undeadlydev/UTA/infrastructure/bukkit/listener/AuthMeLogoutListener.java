package com.undeadlydev.UTA.infrastructure.bukkit.listener;

import com.google.inject.Inject;
import com.undeadlydev.UTA.application.usecase.HandleLogoutUseCase;
import fr.xephi.authme.events.LogoutEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AuthMeLogoutListener implements Listener {

    private final HandleLogoutUseCase useCase;

    @Inject
    public AuthMeLogoutListener(HandleLogoutUseCase useCase) {
        this.useCase = useCase;
    }

    @EventHandler
    public void onLogout(LogoutEvent event) {
        useCase.handle(event.getPlayer());
    }
}
