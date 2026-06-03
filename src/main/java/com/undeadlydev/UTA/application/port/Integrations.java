package com.undeadlydev.UTA.application.port;

/** Optional third-party integrations (PlaceholderAPI, CMILib, FastLogin) that can be re-detected on reload. */
public interface Integrations {

    void reload();

    boolean isPlaceholderApiEnabled();

    boolean isCmiLibEnabled();

    boolean isFastLoginEnabled();
}
