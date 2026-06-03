package com.undeadlydev.UTA.infrastructure.bukkit.notification;

import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.notification.MessageNotifier;
import com.undeadlydev.UTA.infrastructure.text.CenterMessage;
import org.bukkit.entity.Player;

/** {@link MessageNotifier} that splits multi-line text and optionally centers each line. */
@Singleton
public class ChatMessageNotifier implements MessageNotifier {

    @Override
    public void send(Player player, String message, boolean centered) {
        if (centered) {
            for (String line : message.split("\\n")) {
                player.sendMessage(CenterMessage.getCenteredMessage(line));
            }
        } else {
            player.sendMessage(message);
        }
    }
}
