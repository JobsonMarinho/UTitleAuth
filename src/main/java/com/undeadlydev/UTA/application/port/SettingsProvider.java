package com.undeadlydev.UTA.application.port;

import com.undeadlydev.UTA.domain.model.AuthStage;
import com.undeadlydev.UTA.domain.model.BossBarAppearance;
import com.undeadlydev.UTA.domain.model.NotificationChannel;
import com.undeadlydev.UTA.domain.model.TitleTiming;

/**
 * Read access to {@code config.yml} plus the few values bridged from AuthMe's own config.
 * All keys mirror the original layout so existing configurations keep working.
 */
public interface SettingsProvider {

    boolean isActionBarEnabled();

    boolean isBossBarEnabled();

    boolean isXpCountdownEnabled();

    /** {@code config.<section>.<stage>.notification.enabled} — used by the one-shot (completion) stages. */
    boolean isStageEnabled(NotificationChannel channel, AuthStage stage);

    TitleTiming titleTiming(AuthStage stage);

    /** {@code config.bossbar.<stage>.time.stay} (seconds). */
    int bossBarStay(AuthStage stage);

    BossBarAppearance bossBarAppearance(AuthStage stage);

    /** {@code config.message.welcome.<stage>.enabled} for completion stages. */
    boolean isWelcomeEnabled(AuthStage stage);

    boolean isWelcomeCentered(AuthStage stage);

    /** {@code config.message.welcome.onjoin.<register|login>.enabled} for the join greeting. */
    boolean isJoinWelcomeEnabled(AuthStage stage);

    boolean isJoinWelcomeCentered(AuthStage stage);

    /** AuthMe {@code settings.restrictions.timeout} — the countdown length in seconds. */
    int authTimeout();

    /** AuthMe {@code settings.registration.forceLoginAfterRegister}. */
    boolean forceLoginAfterRegister();

    void reload();
}
