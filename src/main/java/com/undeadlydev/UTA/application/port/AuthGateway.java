package com.undeadlydev.UTA.application.port;

import org.bukkit.entity.Player;

/** Abstraction over the authentication provider (AuthMe). */
public interface AuthGateway {

    boolean isRegistered(String name);

    boolean isRegistered(Player player);

    boolean isAuthenticated(Player player);
}
