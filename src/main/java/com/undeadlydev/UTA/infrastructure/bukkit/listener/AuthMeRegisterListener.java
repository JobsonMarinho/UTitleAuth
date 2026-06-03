package com.undeadlydev.UTA.infrastructure.bukkit.listener;

import com.google.inject.Inject;
import com.undeadlydev.UTA.application.usecase.HandleRegisterUseCase;
import fr.xephi.authme.events.RegisterEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AuthMeRegisterListener implements Listener {

    private final HandleRegisterUseCase useCase;

    @Inject
    public AuthMeRegisterListener(HandleRegisterUseCase useCase) {
        this.useCase = useCase;
    }

    @EventHandler
    public void onRegister(RegisterEvent event) {
        useCase.handle(event.getPlayer());
    }
}
