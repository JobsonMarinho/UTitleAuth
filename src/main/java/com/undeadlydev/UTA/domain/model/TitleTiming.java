package com.undeadlydev.UTA.domain.model;

import lombok.Value;

/** Immutable fade-in / stay / fade-out timing (in ticks) for a title. */
@Value
public class TitleTiming {

    int fadeIn;
    int stay;
    int fadeOut;

    /** Timing used for pending stages: appear instantly and stay until cleared. */
    public static TitleTiming persistent() {
        return new TitleTiming(0, 999999999, 20);
    }
}
