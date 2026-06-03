package com.undeadlydev.UTA.application.port;

import org.bukkit.entity.Player;

/** Resolves localized/colored strings from {@code lang.yml}. */
public interface MessageProvider {

    /** Raw key lookup with color codes applied. */
    String get(String key);

    /** Key lookup personalized for a player ({@code {player}} substitution + PlaceholderAPI). */
    String get(Player player, String key);

    void reload();
}
