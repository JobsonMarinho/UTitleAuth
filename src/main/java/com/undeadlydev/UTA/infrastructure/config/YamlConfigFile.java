package com.undeadlydev.UTA.infrastructure.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * A YAML file shipped as a jar resource, copied to the data folder on first run and kept in sync
 * with bundled defaults. Generic replacement for the old {@code FileManager}.
 */
public class YamlConfigFile {

    private final File file;
    private final String resourceName;
    private final Plugin plugin;
    private YamlConfiguration config;

    public YamlConfigFile(Plugin plugin, String name) {
        this.plugin = plugin;
        this.resourceName = name + ".yml";
        this.file = new File(plugin.getDataFolder(), resourceName);
        load(true);
    }

    private void load(boolean mergeDefaults) {
        this.config = YamlConfiguration.loadConfiguration(file);
        YamlConfiguration defaults = loadDefaults();
        try {
            if (!file.exists()) {
                if (defaults != null) {
                    config.addDefaults(defaults);
                    config.options().copyDefaults(true);
                }
                config.save(file);
            } else {
                if (mergeDefaults && defaults != null) {
                    config.addDefaults(defaults);
                    config.options().copyDefaults(true);
                    config.save(file);
                }
                config.load(file);
            }
        } catch (IOException | InvalidConfigurationException ignored) {
            // Keep whatever loaded; a malformed file should not crash the plugin.
        }
    }

    private YamlConfiguration loadDefaults() {
        InputStream resource = plugin.getResource(resourceName);
        if (resource == null) {
            return null;
        }
        try (Reader reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
            return YamlConfiguration.loadConfiguration(reader);
        } catch (IOException e) {
            return null;
        }
    }

    public void reload() {
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().warning("Failed to reload " + resourceName + ": " + e.getMessage());
        }
    }

    public String getString(String path) {
        return config.getString(path);
    }
}
