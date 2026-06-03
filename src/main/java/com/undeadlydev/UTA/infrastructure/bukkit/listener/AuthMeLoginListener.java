package com.undeadlydev.UTA.infrastructure.bukkit.listener;

import com.google.inject.Inject;
import com.undeadlydev.UTA.application.usecase.HandleLoginUseCase;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AuthMeLoginListener implements Listener {

    private final HandleLoginUseCase useCase;

    @Inject
    public AuthMeLoginListener(HandleLoginUseCase useCase) {
        this.useCase = useCase;
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        useCase.handle(event.getPlayer());
    }
}
