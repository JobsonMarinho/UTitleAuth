package com.undeadlydev.UTA;

import com.google.common.collect.Sets;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.PlatformScheduler;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import com.undeadlydev.UTA.managers.*;
import com.undeadlydev.UTA.listeners.*;
import com.undeadlydev.UTA.superclass.SpigotUpdater;
import com.undeadlydev.UTA.utils.ChatUtils;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.undeadlydev.UTA.cmds.utitleauthCMD;

import java.util.*;

public class Main extends JavaPlugin {

    private static Main instance;

    private AddonManager adm;
    private TitlesManager tm;
    private ActionBarManager ac;
    private BossBarManager bm;
    private WelcomeMessageManager wm;
    private LevelManager lm;
    private FileManager lang;
    private final Set<UUID> SecurePlayerRegister = Sets.newHashSet();
    private final Set<UUID> SecurePlayerLogin = Sets.newHashSet();
    private final Map<String, WrappedTask> cancelac = new HashMap<>();
    private final Map<String, WrappedTask> cancelboss = new HashMap<>();
    private final Map<UUID, LevelManager> levelManagers = new HashMap<>();
    private final FoliaLib foliaLib;
    private int pluginId;
    private int resourceId;

    public static Main get() {
        return instance;
    }
    public AddonManager getAdm() {
        return adm;
    }
    public FileManager getLang() {
        return lang;
    }
    public Set<UUID> getRegisterSecure() {
        return SecurePlayerRegister;
    }
    public void addRegisterSecure(Player uuid){
        this.SecurePlayerRegister.add(uuid.getUniqueId());
    }
    public Set<UUID> getLoginSecure() {
        return SecurePlayerLogin;
    }
    public void addLoginSecure(Player uuid){
        this.SecurePlayerLogin.add(uuid.getUniqueId());
    }
    public TitlesManager getTm() {
        return tm;
    }
    public ActionBarManager getAcM() {
        return ac;
    }
    public BossBarManager getBM() {
        return bm;
    }
    public WelcomeMessageManager getWm() {
        return wm;
    }
    public LevelManager getLm() {
        return lm;
    }
    public Map<String, WrappedTask> cancelAc() {
        return cancelac;
    }
    public Map<String, WrappedTask> cancelBoss() {
        return cancelboss;
    }
    public Map<UUID, LevelManager> getLevelManagers() {
        return levelManagers;
    }
    public int getResourceId() {
        return resourceId;
    }
    public PlatformScheduler getScheduler() {
        return foliaLib.getScheduler();
    }

    public static FileConfiguration getOtherConfig() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("AuthMe");
        FileConfiguration config = p.getConfig();
        return config;
    }

    public Main() {
        foliaLib = new FoliaLib(this);
    }

    public void onEnable() {
        instance = this;
        pluginId = 14756;
        resourceId = 88058;
        PluginManager pm = getServer().getPluginManager();
        getConfig().options().copyDefaults(true);
        saveConfig();
        lang = new FileManager("lang", true);
        adm = new AddonManager();
        tm = new TitlesManager(this);
        ac = new ActionBarManager(this);
        bm = new BossBarManager(this);
        wm = new WelcomeMessageManager(this);
        lm = new LevelManager(this);
        new utitleauthCMD(this);
        adm.reload();
        pm.registerEvents(new GeneralListeners(this), this);
        pm.registerEvents(new LoginListener(this), this);
        pm.registerEvents(new LogoutListener(this), this);
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new RegisterListener(this), this);
        pm.registerEvents(new UnregisterbyAdminListener(this), this);
        pm.registerEvents(new UnregisterListener(this), this);
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
        sendLogMessage(" ");
        sendLogMessage("&fServer: &c" + getServer().getName() + " " + getServer().getBukkitVersion());
        sendLogMessage("&fSuccessfully Plugin &aEnabled! &cv" + getDescription().getVersion());
        sendLogMessage("&fCreator: &eUnDeadlyDev");
        sendLogMessage("&fThanks for use my plugin :D");
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
        loadMetrics();
        CheckUpdate();
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
        getScheduler().cancelAllTasks();
        sendLogMessage("&7-----------------------------------");
        sendLogMessage(" ");
        sendLogMessage("&fSuccessfully Plugin &cDisable!");
        sendLogMessage("&fCreator: &eUnDeadlyDev");
        sendLogMessage("&fThanks for use my plugin :D");
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
    }

    public void sendLogMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatUtils.parseLegacy("&7[&e&lUTitleAuth&7] &8| " + msg));
    }

    public void loadMetrics() {
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SimplePie("placeholderapi_enabled", () -> adm.isPlaceholderAPIEnabled() ? "Yes" : "No"));
        metrics.addCustomChart(new SimplePie("fastlogin_enabled", () -> adm.isFastLoginEnabled() ? "Yes" : "No"));
        metrics.addCustomChart(new SimplePie("CMILib_enabled", () -> adm.isCMILibEnabled() ? "Yes" : "No"));
    }

    private void CheckUpdate() {
        if (getConfig().getBoolean("update-check")) {
            getScheduler().runAsync((task) -> {
                SpigotUpdater updater = new SpigotUpdater(instance, resourceId);
                try {
                    if (updater.checkForUpdates()) {
                        Bukkit.getConsoleSender().sendMessage(getLang().get("message.notifyUpdate").replace("{CURRENT}", getDescription().getVersion()).replace("{NEW}", updater.getLatestVersion()).replace("{LINK}", updater.getResourceURL()));
                    }
                } catch (Exception e) {
                    sendLogMessage("Failed to check for a update on spigot.");
                }
            });
        }
    }
}