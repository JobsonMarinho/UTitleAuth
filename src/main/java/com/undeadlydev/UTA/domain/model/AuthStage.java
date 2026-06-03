package com.undeadlydev.UTA.domain.model;

/**
 * The authentication stage a player is in, which drives every notification the plugin sends.
 *
 * <p>The {@code key} matches the section used in both {@code config.yml} and {@code lang.yml}
 * (e.g. {@code titles.<key>}, {@code actionbar.<key>}, {@code bossbar.<key>}).</p>
 *
 * <p>"Pending" stages ({@link #NO_REGISTER} / {@link #NO_LOGIN}) are shown continuously while the
 * player still has to act (persistent title + repeating action bar/boss bar countdown). The other
 * stages are one-shot confirmations rendered with configurable timings.</p>
 */
public enum AuthStage {

    NO_REGISTER("noregister", true),
    NO_LOGIN("nologin", true),
    REGISTER("register", false),
    LOGIN("login", false),
    AUTOLOGIN("autologin", false);

    private final String key;
    private final boolean pending;

    AuthStage(String key, boolean pending) {
        this.key = key;
        this.pending = pending;
    }

    public String key() {
        return key;
    }

    /** Whether this stage represents an action still owed by the player (continuous notifications). */
    public boolean isPending() {
        return pending;
    }
}
