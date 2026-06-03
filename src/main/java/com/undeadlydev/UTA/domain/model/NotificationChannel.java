package com.undeadlydev.UTA.domain.model;

/**
 * The output channels through which an {@link AuthStage} can be announced to a player.
 * Each channel maps to a top-level section in {@code config.yml}.
 */
public enum NotificationChannel {

    TITLE("titles"),
    ACTIONBAR("actionbar"),
    BOSSBAR("bossbar"),
    MESSAGE("message");

    private final String configSection;

    NotificationChannel(String configSection) {
        this.configSection = configSection;
    }

    public String configSection() {
        return configSection;
    }
}
