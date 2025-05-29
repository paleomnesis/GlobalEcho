package me.paleomnesis.globalEcho.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalEchoTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("islandannouncement")) return List.of();

        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>(Arrays.asList("guide", "announcement"));

            if (sender.hasPermission("globalecho.reload")) {
                suggestions.add("reload");
            }

            return suggestions.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        return List.of();
    }
}