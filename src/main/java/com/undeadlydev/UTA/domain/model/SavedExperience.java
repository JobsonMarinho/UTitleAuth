package com.undeadlydev.UTA.domain.model;

import lombok.Value;

/** A player's real level/exp, captured before the XP-countdown animation overwrites the bar. */
@Value
public class SavedExperience {

    int level;
    float exp;
}
