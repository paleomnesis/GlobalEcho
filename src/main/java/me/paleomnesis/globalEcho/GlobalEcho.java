package me.paleomnesis.globalEcho;

import me.paleomnesis.globalEcho.commands.GlobalEchoCommand;
import me.paleomnesis.globalEcho.commands.GlobalEchoTabCompleter;
import me.paleomnesis.globalEcho.managers.CooldownManager;
import me.paleomnesis.globalEcho.managers.EconomyManager;
import me.paleomnesis.globalEcho.managers.MessageManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class GlobalEcho extends JavaPlugin {

    private static GlobalEcho instance;

    private MiniMessage miniMessage;
    private FileConfiguration messagesConfig;
    private File messagesFile;

    private CooldownManager cooldownManager;
    private EconomyManager economyManager;
    private MessageManager messageManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        loadMessagesConfig();

        miniMessage = MiniMessage.miniMessage();

        cooldownManager = new CooldownManager();
        economyManager = new EconomyManager();
        economyManager.setupEconomy();
        messageManager = new MessageManager();

        getCommand("islandannouncement").setExecutor(new GlobalEchoCommand(this));
        getCommand("islandannouncement").setTabCompleter(new GlobalEchoTabCompleter());
    }

    @Override
    public void onDisable() {
        if (cooldownManager != null) {
            cooldownManager.shutdown();
        }
    }

    public void loadMessagesConfig() {
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public static GlobalEcho getInstance() {
        return instance;
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}
