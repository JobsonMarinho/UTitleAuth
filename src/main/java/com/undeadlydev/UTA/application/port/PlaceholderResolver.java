package com.undeadlydev.UTA.application.port;

import org.bukkit.entity.Player;

/** Resolves third-party placeholders (PlaceholderAPI) in a string; a no-op when PAPI is absent. */
public interface PlaceholderResolver {

    String resolve(Player player, String text);
}
