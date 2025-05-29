package me.paleomnesis.globalEcho.commands.subcommans;

import me.paleomnesis.globalEcho.GlobalEcho;
import me.paleomnesis.globalEcho.managers.MessageManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuideSubCommand {

    private final MessageManager messages;

    public GuideSubCommand(GlobalEcho plugin) {
        this.messages = plugin.getMessageManager();
    }

    public boolean execute(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            messages.send(sender, "errors.player-only");
            return true;
        }

        messages.sendList(player, "guide");
        return true;
    }
}