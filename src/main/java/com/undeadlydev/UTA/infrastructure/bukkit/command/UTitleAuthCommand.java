package com.undeadlydev.UTA.infrastructure.bukkit.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.undeadlydev.UTA.application.port.MessageProvider;
import com.undeadlydev.UTA.application.usecase.ReloadUseCase;
import com.undeadlydev.UTA.infrastructure.text.HexUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/** {@code /utitleauth reload [config|lang]} — admin command for reloading the plugin. */
@Singleton
public class UTitleAuthCommand extends AbstractPluginCommand {

    private static final String PREFIX = "&e[UTitleAuth] ";

    private final ReloadUseCase reload;
    private final MessageProvider messages;

    @Inject
    public UTitleAuthCommand(JavaPlugin plugin, ReloadUseCase reload, MessageProvider messages) {
        super(plugin, "utitleauth");
        this.reload = reload;
        this.messages = messages;

        setPermission("utitleauth.admin");
        setPermissionMessage(messages.get("message.noPermission"));
        addTabSimple(0, "reload");
        addTabWithContext(1, null, "reload", "config", "lang");
        register();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || !args[0].equalsIgnoreCase("reload")) {
            sendHelp(sender);
            return true;
        }

        if (args.length == 1) {
            reload.reloadAll();
            send(sender, "message.reload");
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "lang":
                reload.reloadLang();
                send(sender, "message.reloadLang");
                break;
            case "config":
                reload.reloadConfig();
                send(sender, "message.reload");
                break;
            default:
                sendHelp(sender);
                break;
        }
        return true;
    }

    private void send(CommandSender sender, String key) {
        sender.sendMessage(HexUtils.colorify(PREFIX) + messages.get(key));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(HexUtils.colorify(PREFIX + "&c&lAdmin Commands."));
        sender.sendMessage(HexUtils.colorify(PREFIX + "&e/utitleauth reload &7(Reload all configs)"));
        sender.sendMessage(HexUtils.colorify(PREFIX + " "));
        sender.sendMessage(HexUtils.colorify(PREFIX + "&e/utitleauth reload config &7(Reload only config file)"));
        sender.sendMessage(HexUtils.colorify(PREFIX + "&e/utitleauth reload lang &7(Reload only lang file)"));
    }
}
