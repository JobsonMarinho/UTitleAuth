package com.undeadlydev.UTA.placeholders;

import fr.xephi.authme.api.v3.AuthMeApi;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class Placeholders extends PlaceholderExpansion {

    public Placeholders() {
    }

    @Nonnull
    public String getIdentifier() {
        return "utitleauth";
    }

    @Nonnull
    public String getAuthor() {
        return "UnDeadlyDev";
    }

    @Nonnull
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, @Nonnull String id) {
        if (p == null || !p.isOnline())
            return null;

        // %utitleauth_status% -> register/login
        if (id.equalsIgnoreCase("status")) {
            return AuthMeApi.getInstance().isRegistered(p.getName()) ? "login" : "register";
        }

        // %utitleauth_login% -> true/false
        if (id.equalsIgnoreCase("login")) {
            return String.valueOf(AuthMeApi.getInstance().isAuthenticated(p));
        }

        return null;
    }
}
