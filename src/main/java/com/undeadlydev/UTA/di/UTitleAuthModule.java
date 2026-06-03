package com.undeadlydev.UTA.di;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.tcoded.folialib.impl.PlatformScheduler;
import com.undeadlydev.UTA.application.port.AuthGateway;
import com.undeadlydev.UTA.application.port.Integrations;
import com.undeadlydev.UTA.application.port.MessageProvider;
import com.undeadlydev.UTA.application.port.PlaceholderResolver;
import com.undeadlydev.UTA.application.port.PlayerScheduler;
import com.undeadlydev.UTA.application.port.PremiumGateway;
import com.undeadlydev.UTA.application.port.SessionRepository;
import com.undeadlydev.UTA.application.port.SettingsProvider;
import com.undeadlydev.UTA.application.port.notification.ActionBarNotifier;
import com.undeadlydev.UTA.application.port.notification.BossBarNotifier;
import com.undeadlydev.UTA.application.port.notification.ExperienceBarNotifier;
import com.undeadlydev.UTA.application.port.notification.MessageNotifier;
import com.undeadlydev.UTA.application.port.notification.TitleNotifier;
import com.undeadlydev.UTA.domain.service.AuthStageResolver;
import com.undeadlydev.UTA.infrastructure.auth.AuthMeGateway;
import com.undeadlydev.UTA.infrastructure.bukkit.notification.BukkitBossBarNotifier;
import com.undeadlydev.UTA.infrastructure.bukkit.notification.BukkitExperienceBarNotifier;
import com.undeadlydev.UTA.infrastructure.bukkit.notification.ChatMessageNotifier;
import com.undeadlydev.UTA.infrastructure.bukkit.notification.XSeriesActionBarNotifier;
import com.undeadlydev.UTA.infrastructure.bukkit.notification.XSeriesTitleNotifier;
import com.undeadlydev.UTA.infrastructure.config.YamlMessageProvider;
import com.undeadlydev.UTA.infrastructure.config.YamlSettingsProvider;
import com.undeadlydev.UTA.infrastructure.integration.AddonRegistry;
import com.undeadlydev.UTA.infrastructure.premium.FastLoginPremiumGateway;
import com.undeadlydev.UTA.infrastructure.scheduler.FoliaLibPlayerScheduler;
import com.undeadlydev.UTA.infrastructure.session.InMemorySessionRepository;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/** Wires every application port to its infrastructure adapter. */
public class UTitleAuthModule extends AbstractModule {

    private final JavaPlugin plugin;
    private final PlatformScheduler scheduler;

    public UTitleAuthModule(JavaPlugin plugin, PlatformScheduler scheduler) {
        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    @Override
    protected void configure() {
        // Platform instances
        bind(JavaPlugin.class).toInstance(plugin);
        bind(Plugin.class).toInstance(plugin);
        bind(PlatformScheduler.class).toInstance(scheduler);

        // Domain services
        bind(AuthStageResolver.class).in(Singleton.class);

        // Gateways
        bind(AuthGateway.class).to(AuthMeGateway.class).in(Singleton.class);
        bind(PremiumGateway.class).to(FastLoginPremiumGateway.class).in(Singleton.class);

        // Scheduling & state
        bind(PlayerScheduler.class).to(FoliaLibPlayerScheduler.class).in(Singleton.class);
        bind(SessionRepository.class).to(InMemorySessionRepository.class).in(Singleton.class);

        // Config & messages
        bind(SettingsProvider.class).to(YamlSettingsProvider.class).in(Singleton.class);
        bind(MessageProvider.class).to(YamlMessageProvider.class).in(Singleton.class);

        // Integrations (same singleton serves both roles)
        bind(AddonRegistry.class).in(Singleton.class);
        bind(Integrations.class).to(AddonRegistry.class);
        bind(PlaceholderResolver.class).to(AddonRegistry.class);

        // Notification channels
        bind(TitleNotifier.class).to(XSeriesTitleNotifier.class).in(Singleton.class);
        bind(ActionBarNotifier.class).to(XSeriesActionBarNotifier.class).in(Singleton.class);
        bind(BossBarNotifier.class).to(BukkitBossBarNotifier.class).in(Singleton.class);
        bind(MessageNotifier.class).to(ChatMessageNotifier.class).in(Singleton.class);
        bind(ExperienceBarNotifier.class).to(BukkitExperienceBarNotifier.class).in(Singleton.class);
    }
}
