package me.paleomnesis.globalEcho.managers;

import me.paleomnesis.globalEcho.GlobalEcho;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class CooldownDatabaseManager {

    private Connection connection;

    public void setupDatabase() {
        try {
            File dbFile = new File(GlobalEcho.getInstance().getDataFolder(), "cooldowns.db");
            if (!dbFile.exists()) {
                dbFile.getParentFile().mkdirs();
                dbFile.createNewFile();
            }

            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS cooldowns (" +
                                "uuid TEXT PRIMARY KEY, " +
                                "last_used BIGINT" +
                                ");"
                );
            }

            GlobalEcho.getInstance().getLogger().info("Cooldown veritabanı başarıyla yüklendi.");

        } catch (Exception e) {
            GlobalEcho.getInstance().getLogger().severe("Cooldown veritabanı oluşturulamadı:");
            e.printStackTrace();
        }
    }

    public void setLastUsed(UUID uuid, long timestamp) {
        String sql = "INSERT OR REPLACE INTO cooldowns (uuid, last_used) VALUES (?, ?);";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setLong(2, timestamp);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getLastUsed(UUID uuid) {
        String sql = "SELECT last_used FROM cooldowns WHERE uuid = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("last_used");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
