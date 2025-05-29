package me.paleomnesis.globalEcho.commands;

import me.paleomnesis.globalEcho.GlobalEcho;
import me.paleomnesis.globalEcho.commands.subcommans.AnnouncementSubCommand;
import me.paleomnesis.globalEcho.commands.subcommans.GuideSubCommand;
import me.paleomnesis.globalEcho.commands.subcommans.ReloadSubCommand;
import me.paleomnesis.globalEcho.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GlobalEchoCommand implements CommandExecutor {

    private final GlobalEcho plugin;
    private final MessageManager messages;

    public GlobalEchoCommand(GlobalEcho plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessageManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            messages.sendList(sender, "usage.root");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "guide":
                return new GuideSubCommand(plugin).execute(sender);

            case "announcement":
                return new AnnouncementSubCommand(plugin).execute(sender, args);

            case "reload":
                return new ReloadSubCommand(plugin).execute(sender);

            default:
                messages.send(sender, "errors.unknown-sub".replace("%sub%", sub));
                messages.sendList(sender, "usage.root");
                return true;
        }
    }
}
