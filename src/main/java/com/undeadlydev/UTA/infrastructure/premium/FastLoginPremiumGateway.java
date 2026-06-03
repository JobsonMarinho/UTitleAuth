package com.undeadlydev.UTA.infrastructure.premium;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.Integrations;
import com.undeadlydev.UTA.application.port.PremiumGateway;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * {@link PremiumGateway} backed by FastLogin. The {@link Integrations} guard ensures the FastLogin
 * classes are only touched when the plugin is actually present.
 */
@Singleton
public class FastLoginPremiumGateway implements PremiumGateway {

    private final Integrations integrations;

    @Inject
    public FastLoginPremiumGateway(Integrations integrations) {
        this.integrations = integrations;
    }

    @Override
    public boolean isPremium(Player player) {
        if (!integrations.isFastLoginEnabled()) {
            return false;
        }
        return JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(player.getUniqueId()) == PremiumStatus.PREMIUM;
    }
}
