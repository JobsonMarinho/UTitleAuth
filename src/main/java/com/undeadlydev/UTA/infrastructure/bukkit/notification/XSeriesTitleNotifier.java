package com.undeadlydev.UTA.infrastructure.bukkit.notification;

import com.cryptomorin.xseries.messages.Titles;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.Integrations;
import com.undeadlydev.UTA.application.port.notification.TitleNotifier;
import net.Zrips.CMILib.TitleMessages.CMITitleMessage;
import org.bukkit.entity.Player;

/** {@link TitleNotifier} using XSeries, delegating to CMILib's title API when CMILib is present. */
@Singleton
public class XSeriesTitleNotifier implements TitleNotifier {

    private final Integrations integrations;

    @Inject
    public XSeriesTitleNotifier(Integrations integrations) {
        this.integrations = integrations;
    }

    @Override
    public void send(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (integrations.isCmiLibEnabled()) {
            CMITitleMessage.send(player, title, subtitle, fadeIn, stay, fadeOut);
        } else {
            Titles.sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
        }
    }

    @Override
    public void clear(Player player) {
        Titles.clearTitle(player);
    }
}
