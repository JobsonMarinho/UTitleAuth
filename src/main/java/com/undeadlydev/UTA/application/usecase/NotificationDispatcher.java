package com.undeadlydev.UTA.application.usecase;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.MessageProvider;
import com.undeadlydev.UTA.application.port.PlayerScheduler;
import com.undeadlydev.UTA.application.port.SettingsProvider;
import com.undeadlydev.UTA.application.port.notification.ActionBarNotifier;
import com.undeadlydev.UTA.application.port.notification.BossBarNotifier;
import com.undeadlydev.UTA.application.port.notification.MessageNotifier;
import com.undeadlydev.UTA.application.port.notification.TitleNotifier;
import com.undeadlydev.UTA.domain.model.AuthStage;
import com.undeadlydev.UTA.domain.model.BossBarAppearance;
import com.undeadlydev.UTA.domain.model.NotificationChannel;
import com.undeadlydev.UTA.domain.model.TitleTiming;
import org.bukkit.entity.Player;

/**
 * Sends the one-shot confirmation notifications for the completion stages
 * (REGISTER / LOGIN / AUTOLOGIN): a timed title, an action bar, a boss bar that auto-removes,
 * and the welcome message — each gated by its own {@code notification.enabled} flag.
 */
@Singleton
public class NotificationDispatcher {

    private final SettingsProvider settings;
    private final MessageProvider messages;
    private final PlayerScheduler scheduler;
    private final TitleNotifier titles;
    private final ActionBarNotifier actionBars;
    private final BossBarNotifier bossBars;
    private final MessageNotifier chat;

    @Inject
    public NotificationDispatcher(SettingsProvider settings,
                                  MessageProvider messages,
                                  PlayerScheduler scheduler,
                                  TitleNotifier titles,
                                  ActionBarNotifier actionBars,
                                  BossBarNotifier bossBars,
                                  MessageNotifier chat) {
        this.settings = settings;
        this.messages = messages;
        this.scheduler = scheduler;
        this.titles = titles;
        this.actionBars = actionBars;
        this.bossBars = bossBars;
        this.chat = chat;
    }

    public void sendCompletion(Player player, AuthStage stage) {
        String key = stage.key();

        titles.clear(player);
        if (settings.isStageEnabled(NotificationChannel.TITLE, stage)) {
            TitleTiming timing = settings.titleTiming(stage);
            titles.send(player,
                    messages.get(player, "titles." + key + ".title"),
                    messages.get(player, "titles." + key + ".subtitle"),
                    timing.getFadeIn(), timing.getStay(), timing.getFadeOut());
        }

        if (settings.isActionBarEnabled()) {
            actionBars.clear(player);
            if (settings.isStageEnabled(NotificationChannel.ACTIONBAR, stage)) {
                actionBars.send(player, messages.get(player, "actionbar." + key));
            }
        }

        if (settings.isBossBarEnabled()) {
            bossBars.remove(player);
            if (settings.isStageEnabled(NotificationChannel.BOSSBAR, stage)) {
                BossBarAppearance appearance = settings.bossBarAppearance(stage);
                bossBars.show(player, messages.get(player, "bossbar." + key),
                        appearance.getColor(), appearance.getStyle());
                int stay = settings.bossBarStay(stage);
                if (stay >= 1) {
                    scheduler.runLater(player, () -> bossBars.remove(player), stay * 20L);
                }
            }
        }

        if (settings.isWelcomeEnabled(stage)) {
            chat.send(player, messages.get(player, "message.welcome." + key), settings.isWelcomeCentered(stage));
        }
    }
}
