package com.undeadlydev.UTA.infrastructure.integration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.Integrations;
import com.undeadlydev.UTA.application.port.PlaceholderResolver;
import com.undeadlydev.UTA.infrastructure.placeholder.UTitleAuthExpansion;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Detects optional integrations (PlaceholderAPI, CMILib, FastLogin) from {@code config.yml} and the
 * running server, registers the PlaceholderAPI expansion once, and resolves placeholders.
 */
@Singleton
public class AddonRegistry implements Integrations, PlaceholderResolver {

    private final Plugin plugin;
    private final UTitleAuthExpansion expansion;

    private boolean placeholderApi;
    private boolean cmiLib;
    private boolean fastLogin;
    private boolean expansionRegistered;

    @Inject
    public AddonRegistry(Plugin plugin, UTitleAuthExpansion expansion) {
        this.plugin = plugin;
        this.expansion = expansion;
    }

    @Override
    public void reload() {
        placeholderApi = check("PlaceholderAPI");
        cmiLib = check("CMILib");
        fastLogin = check("FastLogin");

        if (placeholderApi && !expansionRegistered) {
            expansion.register();
            expansionRegistered = true;
        }
    }

    private boolean check(String pluginName) {
        if (!plugin.getConfig().isSet("addons." + pluginName) || !plugin.getConfig().getBoolean("addons." + pluginName)) {
            return false;
        }
        if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
            plugin.getLogger().info("Hooked into " + pluginName + "!");
            return true;
        }
        plugin.getConfig().set("addons." + pluginName, false);
        plugin.saveConfig();
        return false;
    }

    @Override
    public String resolve(Player player, String text) {
        if (placeholderApi && player != null) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }

    @Override
    public boolean isPlaceholderApiEnabled() {
        return placeholderApi;
    }

    @Override
    public boolean isCmiLibEnabled() {
        return cmiLib;
    }

    @Override
    public boolean isFastLoginEnabled() {
        return fastLogin;
    }
}
