package com.undeadlydev.UTA.infrastructure.placeholder;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.AuthGateway;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;

/** PlaceholderAPI expansion exposing {@code %utitleauth_status%} and {@code %utitleauth_login%}. */
@Singleton
public class UTitleAuthExpansion extends PlaceholderExpansion {

    private final AuthGateway auth;
    private final Plugin plugin;

    @Inject
    public UTitleAuthExpansion(AuthGateway auth, Plugin plugin) {
        this.auth = auth;
        this.plugin = plugin;
    }

    @Override
    @Nonnull
    public String getIdentifier() {
        return "utitleauth";
    }

    @Override
    @Nonnull
    public String getAuthor() {
        return "UnDeadlyDev";
    }

    @Override
    @Nonnull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, @Nonnull String id) {
        if (p == null || !p.isOnline()) {
            return null;
        }
        if (id.equalsIgnoreCase("status")) {
            return auth.isRegistered(p) ? "login" : "register";
        }
        if (id.equalsIgnoreCase("login")) {
            return String.valueOf(auth.isAuthenticated(p));
        }
        return null;
    }
}
