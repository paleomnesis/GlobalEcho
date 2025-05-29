package me.paleomnesis.globalEcho.commands.subcommans;

import me.paleomnesis.globalEcho.GlobalEcho;
import me.paleomnesis.globalEcho.managers.CooldownManager;
import me.paleomnesis.globalEcho.managers.EconomyManager;
import me.paleomnesis.globalEcho.managers.MessageManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementSubCommand {

    private final GlobalEcho plugin;
    private final MessageManager messages;
    private final MiniMessage mm;
    private final CooldownManager cooldowns;
    private final EconomyManager economy;
    private final FileConfiguration config;

    public AnnouncementSubCommand(GlobalEcho plugin) {
        this.plugin = plugin;
        this.mm = plugin.getMiniMessage();
        this.messages = plugin.getMessageManager();
        this.cooldowns = plugin.getCooldownManager();
        this.economy = plugin.getEconomyManager();
        this.config = plugin.getConfig();
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            messages.send(sender, "errors.player-only");
            return true;
        }

        if (!player.hasPermission("globalecho.use")) {
            messages.send(player, "errors.no-permission");
            return true;
        }

        if (args.length < 2) {
            messages.send(player, "usage.announcement");
            return true;
        }

        boolean bypassCooldown = player.hasPermission("globalecho.bypass");
        int cooldownSec = config.getInt("cooldown.seconds", 60);

        if (!bypassCooldown && cooldowns.isOnCooldown(player, cooldownSec)) {
            long remaining = cooldowns.getRemainingSeconds(player, cooldownSec);
            String raw = plugin.getMessagesConfig().getString("cooldown.wait", "");
            messages.send(player, raw.replace("%seconds%", String.valueOf(remaining)));
            return true;
        }

        boolean free = player.hasPermission("globalecho.free");
        double cost = config.getDouble("economy.cost", 0.0);

        if (economy.isEconomyEnabled() && cost > 0 && !free) {
            if (!economy.hasEnough(player, cost)) {
                String raw = plugin.getMessagesConfig().getString("economy.insufficient", "");
                messages.send(player, raw.replace("%cost%", economy.format(cost)));
                return true;
            }

            if (!economy.withdraw(player, cost)) {
                messages.send(player, "economy.fail");
                return true;
            }
        }

        String userMessage = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        String rawHeader = config.getString("message.header", "<yellow>Announcement - %player%</yellow>")
                .replace("%player%", player.getName());
        String rawBody = config.getString("message.body", "%message%")
                .replace("%message%", userMessage);

        Component header = mm.deserialize(rawHeader);
        Component body = mm.deserialize(rawBody);

        List<Component> duyuru = new ArrayList<>();
        duyuru.add(Component.empty());
        duyuru.add(header);
        duyuru.add(body);
        duyuru.add(Component.empty());

        Bukkit.getOnlinePlayers().forEach(p -> duyuru.forEach(p::sendMessage));
        Bukkit.getConsoleSender().sendMessage("[GlobalEcho] " + player.getName() + " sena a announcement.");

        if (!bypassCooldown) {
            cooldowns.setLastUsed(player);
        }

        return true;
    }
}
