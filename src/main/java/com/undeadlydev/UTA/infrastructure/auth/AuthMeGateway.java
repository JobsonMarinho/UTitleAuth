package com.undeadlydev.UTA.infrastructure.auth;

import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.AuthGateway;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.entity.Player;

/** {@link AuthGateway} backed by the AuthMe v3 API. */
@Singleton
public class AuthMeGateway implements AuthGateway {

    @Override
    public boolean isRegistered(String name) {
        return AuthMeApi.getInstance().isRegistered(name);
    }

    @Override
    public boolean isRegistered(Player player) {
        return AuthMeApi.getInstance().isRegistered(player.getName());
    }

    @Override
    public boolean isAuthenticated(Player player) {
        return AuthMeApi.getInstance().isAuthenticated(player);
    }
}
