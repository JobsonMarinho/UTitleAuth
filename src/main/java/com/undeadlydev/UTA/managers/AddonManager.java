package com.undeadlydev.UTA.managers;


import com.undeadlydev.UTA.Main;
import com.undeadlydev.UTA.addons.CMILibAddon;
import com.undeadlydev.UTA.addons.FastLoginAddon;
import com.undeadlydev.UTA.addons.PlaceholderAPIAddon;
import com.undeadlydev.UTA.interfaces.CMIAddon;
import com.undeadlydev.UTA.interfaces.FastLAddon;
import com.undeadlydev.UTA.interfaces.PlaceholderAddon;
import com.undeadlydev.UTA.placeholders.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AddonManager {

    private PlaceholderAddon placeholder;

    private CMIAddon cmiaddon;

    private FastLAddon fastaddon;

    public boolean check(String pluginName) {
        Main plugin = Main.get();
        if (plugin.getConfig().isSet("addons." + pluginName) && plugin.getConfig().getBoolean("addons." + pluginName)) {
            if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
                plugin.sendLogMessage("Hooked into §a" + pluginName + "§e!");
                return true;
            } else {
                plugin.getConfig().set("addons." + pluginName, false);
                plugin.saveConfig();
                return false;
            }
        }
        return false;
    }

    public void reload() {
        if (check("PlaceholderAPI")) {
            new Placeholders().register();
            placeholder = new PlaceholderAPIAddon();
        }
        if (check("CMILib")) {
            cmiaddon = new CMILibAddon();
        }
        if (check("FastLogin")) {
            fastaddon = new FastLoginAddon();
        }
    }

    public boolean getCMIAddon() {
        if (cmiaddon != null) {
            return true;
        }
        return false;
    }

    public boolean getFastLAddon() {
        if (fastaddon != null) {
            return true;
        }
        return false;
    }

    public String parsePlaceholders(Player p, String value) {
        if (placeholder != null) {
            value = placeholder.parsePlaceholders(p, value);
        }
        return value;
    }

    public boolean isPlaceholderAPIEnabled() {
        return placeholder != null;
    }

    public boolean isFastLoginEnabled() {
        return  fastaddon != null;
    }

    public boolean isCMILibEnabled() {
        return cmiaddon != null;
    }
}