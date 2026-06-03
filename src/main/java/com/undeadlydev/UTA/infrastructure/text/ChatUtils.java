package com.undeadlydev.UTA.infrastructure.text;

import net.md_5.bungee.api.ChatColor;

public class ChatUtils {

    public static String colorCodes(String message) {
        return HexUtils.colorify(message);
    }

    public static String clearColor(String message) {
        return HexUtils.stripColors(message);
    }

    public static String parseLegacy(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}