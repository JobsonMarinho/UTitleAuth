package com.undeadlydev.UTA.application.usecase;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.AuthGateway;
import com.undeadlydev.UTA.application.port.MessageProvider;
import com.undeadlydev.UTA.application.port.PlayerScheduler;
import com.undeadlydev.UTA.application.port.SessionRepository;
import com.undeadlydev.UTA.application.port.SettingsProvider;
import com.undeadlydev.UTA.application.port.TaskHandle;
import com.undeadlydev.UTA.application.port.notification.ActionBarNotifier;
import com.undeadlydev.UTA.application.port.notification.BossBarNotifier;
import com.undeadlydev.UTA.application.port.notification.ExperienceBarNotifier;
import com.undeadlydev.UTA.application.port.notification.TitleNotifier;
import com.undeadlydev.UTA.domain.model.AuthStage;
import com.undeadlydev.UTA.domain.model.BossBarAppearance;
import com.undeadlydev.UTA.domain.model.SavedExperience;
import com.undeadlydev.UTA.domain.model.TitleTiming;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Owns the lifecycle of the "pending" notifications (NO_REGISTER / NO_LOGIN): the persistent title,
 * the repeating action bar / boss bar countdown, and the join-time XP-bar animation. All repeating
 * tasks self-stop once the player is no longer pending; {@link #stop(Player)} tears everything down
 * and restores the player's real XP.
 */
@Singleton
public class CountdownService {

    private static final String CH_ACTIONBAR = "actionbar";
    private static final String CH_BOSSBAR = "bossbar";
    private static final String CH_XP_TICKER = "xpTicker";
    private static final String CH_XP_ANIM = "xpAnim";

    private final PlayerScheduler scheduler;
    private final SessionRepository sessions;
    private final SettingsProvider settings;
    private final MessageProvider messages;
    private final AuthGateway auth;
    private final TitleNotifier titles;
    private final ActionBarNotifier actionBars;
    private final BossBarNotifier bossBars;
    private final ExperienceBarNotifier experienceBars;

    @Inject
    public CountdownService(PlayerScheduler scheduler,
                            SessionRepository sessions,
                            SettingsProvider settings,
                            MessageProvider messages,
                            AuthGateway auth,
                            TitleNotifier titles,
                            ActionBarNotifier actionBars,
                            BossBarNotifier bossBars,
                            ExperienceBarNotifier experienceBars) {
        this.scheduler = scheduler;
        this.sessions = sessions;
        this.settings = settings;
        this.messages = messages;
        this.auth = auth;
        this.titles = titles;
        this.actionBars = actionBars;
        this.bossBars = bossBars;
        this.experienceBars = experienceBars;
    }

    /** Shows the persistent title and (re)starts the action bar / boss bar countdown for a pending stage. */
    public void startPending(Player player, AuthStage stage) {
        UUID id = player.getUniqueId();
        sessions.setPendingStage(id, stage);

        TitleTiming timing = TitleTiming.persistent();
        titles.send(player,
                messages.get(player, "titles." + stage.key() + ".title"),
                messages.get(player, "titles." + stage.key() + ".subtitle"),
                timing.getFadeIn(), timing.getStay(), timing.getFadeOut());

        if (settings.isActionBarEnabled()) {
            startActionBarCountdown(player, stage);
        } else {
            sessions.cancelTask(id, CH_ACTIONBAR);
        }

        if (settings.isBossBarEnabled()) {
            startBossBarCountdown(player, stage);
        } else {
            sessions.cancelTask(id, CH_BOSSBAR);
        }
    }

    /** Starts the join-time XP-bar countdown animation (once per session). */
    public void startXp(Player player) {
        if (!settings.isXpCountdownEnabled()) {
            return;
        }
        UUID id = player.getUniqueId();
        if (sessions.savedExperience(id).isPresent()) {
            return; // already running — keep the original capture across stage transitions
        }
        sessions.saveExperience(id, new SavedExperience(
                experienceBars.currentLevel(player), experienceBars.currentExp(player)));

        int timeout = settings.authTimeout();
        int durationTicks = Math.max(timeout, 1) * 20;
        int[] remaining = {timeout};
        int[] elapsed = {0};

        TaskHandle ticker = scheduler.runTimer(player, () -> {
            if (!shouldAnimateXp(player, id) || remaining[0] <= 0) {
                finishXp(id, player);
                return;
            }
            experienceBars.setLevel(player, remaining[0]);
            remaining[0]--;
        }, 0L, 20L);
        sessions.storeTask(id, CH_XP_TICKER, ticker);

        TaskHandle animation = scheduler.runTimer(player, () -> {
            if (!shouldAnimateXp(player, id)) {
                finishXp(id, player);
                return;
            }
            float progress = 1.0f - ((float) elapsed[0] / durationTicks);
            experienceBars.setExp(player, Math.min(Math.max(progress, 0.0f), 1.0f));
            elapsed[0] += 2;
            if (elapsed[0] >= durationTicks) {
                experienceBars.setExp(player, 0.0f);
                finishXp(id, player);
            }
        }, 0L, 2L);
        sessions.storeTask(id, CH_XP_ANIM, animation);
    }

    /** Tears down all pending notifications and restores the player's real XP. */
    public void stop(Player player) {
        UUID id = player.getUniqueId();
        finishXp(id, player);
        sessions.cancelTask(id, CH_ACTIONBAR);
        sessions.cancelTask(id, CH_BOSSBAR);
        bossBars.remove(player);
        sessions.clearPending(id);
    }

    private void startActionBarCountdown(Player player, AuthStage stage) {
        UUID id = player.getUniqueId();
        int[] time = {settings.authTimeout()};
        TaskHandle handle = scheduler.runTimer(player, () -> {
            if (!stillPending(player, id, stage) || time[0] <= 0) {
                sessions.cancelTask(id, CH_ACTIONBAR);
                return;
            }
            actionBars.send(player, messages.get(player, "actionbar." + stage.key())
                    .replace("<time>", String.valueOf(time[0])));
            time[0]--;
        }, 0L, 20L);
        sessions.storeTask(id, CH_ACTIONBAR, handle);
    }

    private void startBossBarCountdown(Player player, AuthStage stage) {
        UUID id = player.getUniqueId();
        BossBarAppearance appearance = settings.bossBarAppearance(stage);
        int[] time = {settings.authTimeout()};
        TaskHandle handle = scheduler.runTimer(player, () -> {
            if (!stillPending(player, id, stage) || time[0] <= 0) {
                sessions.cancelTask(id, CH_BOSSBAR);
                return;
            }
            bossBars.show(player, messages.get(player, "bossbar." + stage.key())
                    .replace("<time>", String.valueOf(time[0])), appearance.getColor(), appearance.getStyle());
            time[0]--;
        }, 0L, 20L);
        sessions.storeTask(id, CH_BOSSBAR, handle);
    }

    private boolean stillPending(Player player, UUID id, AuthStage stage) {
        return player.isOnline() && stage == sessions.pendingStage(id).orElse(null);
    }

    private boolean shouldAnimateXp(Player player, UUID id) {
        return player.isOnline() && !auth.isAuthenticated(player) && sessions.pendingStage(id).isPresent();
    }

    private void finishXp(UUID id, Player player) {
        sessions.cancelTask(id, CH_XP_TICKER);
        sessions.cancelTask(id, CH_XP_ANIM);
        sessions.savedExperience(id).ifPresent(saved -> {
            if (player.isOnline()) {
                experienceBars.setLevel(player, saved.getLevel());
                experienceBars.setExp(player, saved.getExp());
            }
        });
        sessions.clearSavedExperience(id);
    }
}
