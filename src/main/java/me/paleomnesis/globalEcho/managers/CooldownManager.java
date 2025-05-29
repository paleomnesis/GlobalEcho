package me.paleomnesis.globalEcho.managers;

import org.bukkit.entity.Player;

import java.util.UUID;

public class CooldownManager {

    private final CooldownDatabaseManager database;

    public CooldownManager() {
        this.database = new CooldownDatabaseManager();
        this.database.setupDatabase();
    }

    public void setLastUsed(Player player) {
        long timestamp = System.currentTimeMillis();
        UUID uuid = player.getUniqueId();
        database.setLastUsed(uuid, timestamp);
    }

    public boolean isOnCooldown(Player player, int cooldownSeconds) {
        long last = database.getLastUsed(player.getUniqueId());
        if (last == -1) return false;

        long elapsed = (System.currentTimeMillis() - last) / 1000;
        return elapsed < cooldownSeconds;
    }

    public long getRemainingSeconds(Player player, int cooldownSeconds) {
        long last = database.getLastUsed(player.getUniqueId());
        if (last == -1) return 0;

        long elapsed = (System.currentTimeMillis() - last) / 1000;
        return Math.max(cooldownSeconds - elapsed, 0);
    }

    public void shutdown() {
        database.close();
    }
}
