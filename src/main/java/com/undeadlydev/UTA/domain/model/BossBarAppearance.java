package com.undeadlydev.UTA.domain.model;

import lombok.Value;

/**
 * Immutable boss bar appearance. Color and style are kept as the raw config strings
 * (e.g. {@code "RED"}, {@code "SOLID"}); the infrastructure adapter maps them to the
 * matching Bukkit enums so the domain stays free of Bukkit types.
 */
@Value
public class BossBarAppearance {

    String color;
    String style;
}
