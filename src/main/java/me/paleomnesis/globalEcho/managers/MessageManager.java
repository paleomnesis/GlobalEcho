package me.paleomnesis.globalEcho.managers;

import me.paleomnesis.globalEcho.GlobalEcho;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {

    private final MiniMessage mm;
    private final FileConfiguration messages;

    public MessageManager() {
        this.mm = GlobalEcho.getInstance().getMiniMessage();
        this.messages = GlobalEcho.getInstance().getMessagesConfig();
    }

    public Component get(String path) {
        String raw = messages.getString(path, "");
        return mm.deserialize(applyPrefix(raw));
    }

    public List<Component> getList(String path) {
        List<String> lines = messages.getStringList(path);
        List<Component> result = new ArrayList<>();
        for (String line : lines) {
            result.add(mm.deserialize(applyPrefix(line)));
        }
        return result;
    }

    public void send(CommandSender sender, String path) {
        sender.sendMessage(get(path));
    }

    public void sendList(CommandSender sender, String path) {
        for (Component line : getList(path)) {
            sender.sendMessage(line);
        }
    }

    private String applyPrefix(String raw) {
        String prefix = messages.getString("prefix", "");
        return raw.replace("%prefix%", prefix);
    }
}
