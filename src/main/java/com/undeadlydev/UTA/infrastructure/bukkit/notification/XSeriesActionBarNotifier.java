package com.undeadlydev.UTA.infrastructure.bukkit.notification;

import com.cryptomorin.xseries.messages.ActionBar;
import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.notification.ActionBarNotifier;
import org.bukkit.entity.Player;

/** {@link ActionBarNotifier} backed by XSeries. */
@Singleton
public class XSeriesActionBarNotifier implements ActionBarNotifier {

    @Override
    public void send(Player player, String message) {
        ActionBar.sendActionBar(player, message);
    }

    @Override
    public void clear(Player player) {
        ActionBar.clearActionBar(player);
    }
}
