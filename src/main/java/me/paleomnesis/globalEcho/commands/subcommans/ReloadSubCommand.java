package me.paleomnesis.globalEcho.commands.subcommans;

import me.paleomnesis.globalEcho.GlobalEcho;
import me.paleomnesis.globalEcho.managers.MessageManager;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand {

    private final GlobalEcho plugin;
    private final MessageManager messages;

    public ReloadSubCommand(GlobalEcho plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessageManager();
    }

    public boolean execute(CommandSender sender) {
        if (!sender.hasPermission("globalecho.reload")) {
            messages.send(sender, "errors.no-permission");
            return true;
        }

        plugin.reloadConfig();
        plugin.loadMessagesConfig();

        messages.send(sender, "reload.success");
        return true;
    }
}
