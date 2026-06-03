package com.undeadlydev.UTA;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.PlatformScheduler;
import com.undeadlydev.UTA.application.port.Integrations;
import com.undeadlydev.UTA.di.UTitleAuthModule;
import com.undeadlydev.UTA.infrastructure.bukkit.command.UTitleAuthCommand;
import com.undeadlydev.UTA.infrastructure.bukkit.listener.AuthMeLoginListener;
import com.undeadlydev.UTA.infrastructure.bukkit.listener.AuthMeLogoutListener;
import com.undeadlydev.UTA.infrastructure.bukkit.listener.AuthMeRegisterListener;
import com.undeadlydev.UTA.infrastructure.bukkit.listener.AuthMeUnregisterListener;
import com.undeadlydev.UTA.infrastructure.bukkit.listener.PlayerCleanupListener;
import com.undeadlydev.UTA.infrastructure.bukkit.listener.PlayerJoinListener;
import com.undeadlydev.UTA.infrastructure.text.ChatUtils;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin entry point. Bootstraps the Guice injector and wires the Bukkit-facing adapters
 * (listeners, command, PlaceholderAPI expansion, metrics) to the application layer.
 */
public class UTitleAuth extends JavaPlugin {

    private static final int BSTATS_PLUGIN_ID = 14756;

    private FoliaLib foliaLib;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        this.foliaLib = new FoliaLib(this);
        PlatformScheduler scheduler = foliaLib.getScheduler();

        Injector injector = Guice.createInjector(new UTitleAuthModule(this, scheduler));

        // Detect optional integrations and register the PlaceholderAPI expansion.
        injector.getInstance(Integrations.class).reload();

        registerListeners(injector);
        injector.getInstance(UTitleAuthCommand.class); // self-registers via the command map
        loadMetrics(injector);

        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
        sendLogMessage(" ");
        sendLogMessage("&fServer: &c" + getServer().getName() + " " + getServer().getBukkitVersion());
        sendLogMessage("&fSuccessfully Plugin &aEnabled! &cv" + getDescription().getVersion());
        sendLogMessage("&fCreator: &eUnDeadlyDev");
        sendLogMessage("&fThanks for use my plugin :D");
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        if (foliaLib != null) {
            foliaLib.getScheduler().cancelAllTasks();
        }
        sendLogMessage("&7-----------------------------------");
        sendLogMessage(" ");
        sendLogMessage("&fSuccessfully Plugin &cDisable!");
        sendLogMessage("&fCreator: &eUnDeadlyDev");
        sendLogMessage("&fThanks for use my plugin :D");
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
    }

    private void registerListeners(Injector injector) {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(injector.getInstance(PlayerJoinListener.class), this);
        pm.registerEvents(injector.getInstance(AuthMeLoginListener.class), this);
        pm.registerEvents(injector.getInstance(AuthMeLogoutListener.class), this);
        pm.registerEvents(injector.getInstance(AuthMeRegisterListener.class), this);
        pm.registerEvents(injector.getInstance(AuthMeUnregisterListener.class), this);
        pm.registerEvents(injector.getInstance(PlayerCleanupListener.class), this);
    }

    private void loadMetrics(Injector injector) {
        Integrations integrations = injector.getInstance(Integrations.class);
        Metrics metrics = new Metrics(this, BSTATS_PLUGIN_ID);
        metrics.addCustomChart(new SimplePie("placeholderapi_enabled",
                () -> integrations.isPlaceholderApiEnabled() ? "Yes" : "No"));
        metrics.addCustomChart(new SimplePie("fastlogin_enabled",
                () -> integrations.isFastLoginEnabled() ? "Yes" : "No"));
        metrics.addCustomChart(new SimplePie("CMILib_enabled",
                () -> integrations.isCmiLibEnabled() ? "Yes" : "No"));
    }

    private void sendLogMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatUtils.parseLegacy("&7[&e&lUTitleAuth&7] &8| " + msg));
    }
}
