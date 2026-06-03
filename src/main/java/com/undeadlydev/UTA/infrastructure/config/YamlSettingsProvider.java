package com.undeadlydev.UTA.infrastructure.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.SettingsProvider;
import com.undeadlydev.UTA.domain.model.AuthStage;
import com.undeadlydev.UTA.domain.model.BossBarAppearance;
import com.undeadlydev.UTA.domain.model.NotificationChannel;
import com.undeadlydev.UTA.domain.model.TitleTiming;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/** {@link SettingsProvider} backed by the plugin's {@code config.yml} plus a few bridged AuthMe values. */
@Singleton
public class YamlSettingsProvider implements SettingsProvider {

    private final Plugin plugin;

    @Inject
    public YamlSettingsProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    private FileConfiguration config() {
        return plugin.getConfig();
    }

    private FileConfiguration authMeConfig() {
        Plugin authMe = Bukkit.getPluginManager().getPlugin("AuthMe");
        return authMe == null ? null : authMe.getConfig();
    }

    @Override
    public boolean isActionBarEnabled() {
        return config().getBoolean("config.actionbar.enabled");
    }

    @Override
    public boolean isBossBarEnabled() {
        return config().getBoolean("config.bossbar.enabled");
    }

    @Override
    public boolean isXpCountdownEnabled() {
        return config().getBoolean("config.xpcountdownanimation.enabled");
    }

    @Override
    public boolean isStageEnabled(NotificationChannel channel, AuthStage stage) {
        return config().getBoolean("config." + channel.configSection() + "." + stage.key() + ".notification.enabled");
    }

    @Override
    public TitleTiming titleTiming(AuthStage stage) {
        String base = "config.titles." + stage.key() + ".time.";
        return new TitleTiming(
                config().getInt(base + "fadein"),
                config().getInt(base + "stay"),
                config().getInt(base + "fadeout"));
    }

    @Override
    public int bossBarStay(AuthStage stage) {
        return config().getInt("config.bossbar." + stage.key() + ".time.stay");
    }

    @Override
    public BossBarAppearance bossBarAppearance(AuthStage stage) {
        String base = "config.bossbar." + stage.key() + ".";
        String color = orFallback(config().getString(base + "color"), config().getString("config.bossbar.color"));
        String style = orFallback(config().getString(base + "style"), config().getString("config.bossbar.style"));
        return new BossBarAppearance(color, style);
    }

    private String orFallback(String value, String fallback) {
        return (value == null || value.isEmpty()) ? fallback : value;
    }

    @Override
    public boolean isWelcomeEnabled(AuthStage stage) {
        return config().getBoolean("config.message.welcome." + stage.key() + ".enabled");
    }

    @Override
    public boolean isWelcomeCentered(AuthStage stage) {
        return config().getBoolean("config.message.welcome." + stage.key() + ".center");
    }

    @Override
    public boolean isJoinWelcomeEnabled(AuthStage stage) {
        return config().getBoolean("config.message.welcome.onjoin." + joinKey(stage) + ".enabled");
    }

    @Override
    public boolean isJoinWelcomeCentered(AuthStage stage) {
        return config().getBoolean("config.message.welcome.onjoin." + joinKey(stage) + ".center");
    }

    private String joinKey(AuthStage stage) {
        return stage == AuthStage.NO_REGISTER ? "register" : "login";
    }

    @Override
    public int authTimeout() {
        FileConfiguration authMe = authMeConfig();
        return authMe == null ? 0 : authMe.getInt("settings.restrictions.timeout");
    }

    @Override
    public boolean forceLoginAfterRegister() {
        FileConfiguration authMe = authMeConfig();
        return authMe != null && authMe.getBoolean("settings.registration.forceLoginAfterRegister");
    }

    @Override
    public void reload() {
        plugin.reloadConfig();
    }
}
