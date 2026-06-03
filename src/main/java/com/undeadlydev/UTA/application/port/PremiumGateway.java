package com.undeadlydev.UTA.application.port;

import org.bukkit.entity.Player;

/** Abstraction over premium/auto-login detection (FastLogin). The no-op impl reports everyone offline-mode. */
public interface PremiumGateway {

    boolean isPremium(Player player);
}
