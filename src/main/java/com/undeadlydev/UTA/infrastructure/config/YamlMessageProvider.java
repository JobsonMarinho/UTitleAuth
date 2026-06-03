package com.undeadlydev.UTA.infrastructure.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.MessageProvider;
import com.undeadlydev.UTA.application.port.PlaceholderResolver;
import com.undeadlydev.UTA.infrastructure.text.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/** {@link MessageProvider} backed by {@code lang.yml}, applying {@code {player}}, PlaceholderAPI and color codes. */
@Singleton
public class YamlMessageProvider implements MessageProvider {

    private final YamlConfigFile lang;
    private final PlaceholderResolver placeholders;

    @Inject
    public YamlMessageProvider(Plugin plugin, PlaceholderResolver placeholders) {
        this.lang = new YamlConfigFile(plugin, "lang");
        this.placeholders = placeholders;
    }

    @Override
    public String get(String key) {
        String value = lang.getString(key);
        if (value == null) {
            return "";
        }
        return ChatUtils.colorCodes(value);
    }

    @Override
    public String get(Player player, String key) {
        String value = lang.getString(key);
        if (value == null) {
            return "";
        }
        if (player != null) {
            value = placeholders.resolve(player, value.replace("{player}", player.getName()));
        }
        return ChatUtils.colorCodes(value);
    }

    @Override
    public void reload() {
        lang.reload();
    }
}
