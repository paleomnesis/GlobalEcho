package me.paleomnesis.globalEcho.managers;

import me.paleomnesis.globalEcho.GlobalEcho;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {

    private Economy economy;

    public void setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            GlobalEcho.getInstance().getLogger().warning("Vault plugin not found. Economy support is disabled.");
            return;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            GlobalEcho.getInstance().getLogger().warning("Vault economy provider not found.");
            return;
        }

        economy = rsp.getProvider();
        GlobalEcho.getInstance().getLogger().info("Vault economy successfully loaded.");
    }

    public boolean isEconomyEnabled() {
        return GlobalEcho.getInstance().getConfig().getBoolean("economy.enabled", false) && economy != null;
    }

    public boolean hasEnough(OfflinePlayer player, double amount) {
        return isEconomyEnabled() && economy.has(player, amount);
    }

    public boolean withdraw(OfflinePlayer player, double amount) {
        if (!isEconomyEnabled()) return false;
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    public String format(double amount) {
        if (!isEconomyEnabled()) return String.valueOf(amount);
        return economy.format(amount);
    }
}
