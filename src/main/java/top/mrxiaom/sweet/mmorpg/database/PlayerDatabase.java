package top.mrxiaom.sweet.mmorpg.database;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.DatabaseHolder;
import top.mrxiaom.pluginbase.database.IDatabase;
import top.mrxiaom.pluginbase.utils.Util;
import top.mrxiaom.sweet.mmorpg.SweetMMORPG;
import top.mrxiaom.sweet.mmorpg.api.ResourceData;
import top.mrxiaom.sweet.mmorpg.func.AbstractPluginHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PlayerDatabase extends AbstractPluginHolder implements IDatabase, Listener {
    private String table;
    Map<UUID, ResourceData> cache = new HashMap<>();
    double manaRatio, staminaRatio;
    public PlayerDatabase(SweetMMORPG plugin) {
        super(plugin);
        registerEvents();
        register();
    }

    public double getManaRatio() {
        return manaRatio;
    }

    public double getStaminaRatio() {
        return staminaRatio;
    }

    @Override
    public int priority() {
        return 1001;
    }

    @Override
    public void reloadConfig(MemoryConfiguration config) {
        manaRatio = config.getDouble("default-ratio.mana", 75) / 100.0;
        staminaRatio = config.getDouble("default-ratio.stamina", 75) / 100.0;
        // 重新注册 stat modifier
        for (ResourceData data : cache.values()) {
            data.reapplyModifiers();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        load(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        ResourceData data = cache.remove(e.getPlayer().getUniqueId());
        if (data != null) {
            save(data);
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        ResourceData data = cache.remove(e.getPlayer().getUniqueId());
        if (data != null) {
            save(data);
        }
    }

    public Collection<ResourceData> getCaches() {
        return Collections.unmodifiableCollection(cache.values());
    }

    @Nullable
    public ResourceData getOrCached(OfflinePlayer player) {
        if (player == null) return null;
        return getOrCached(player.getUniqueId());
    }

    public ResourceData getOrCached(UUID uuid) {
        ResourceData cached = cache.get(uuid);
        if (cached != null) {
            return cached;
        }
        return load(uuid);
    }

    @Override
    public void reload(Connection conn, String prefix) throws SQLException {
        table = (prefix + "playerdata").toLowerCase();
        try (PreparedStatement ps = conn.prepareStatement(
                "CREATE TABLE if NOT EXISTS `" + table + "`(" +
                        "`uuid` VARCHAR(48) PRIMARY KEY," +
                        "`name` VARCHAR(48)," +
                        "`mana` DECIMAL(10, 2)," +
                        "`stamina` DECIMAL(10, 2)" +
                ");"
        )) {
            ps.execute();
        }
    }

    public ResourceData load(OfflinePlayer player) {
        return load(player.getUniqueId());
    }

    public ResourceData load(UUID uuid) {
        boolean error = false;
        try (Connection conn = plugin.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM `" + table + "` WHERE `uuid`=?;"
            )) {
            ps.setString(1, uuid.toString());
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    double mana = resultSet.getDouble("mana");
                    double stamina = resultSet.getDouble("stamina");
                    ResourceData data = getOrCreateData(uuid, mana, stamina, true);
                    data.setMana(mana);
                    data.setStamina(stamina);
                    return data;
                }
            }
        } catch (SQLException e) {
            error = true;
            warn(e);
        }
        ResourceData data = getOrCreateData(uuid, null, null, false);
        if (!error) {
            save(data);
            data.reapplyModifiers();
        }
        return data;
    }

    private ResourceData getOrCreateData(UUID uuid, Double defaultMana, Double defaultStamina, boolean setIfCached) {
        ResourceData data = cache.get(uuid);
        if (data == null) {
            data = new ResourceData(MMOPlayerData.get(uuid), defaultMana, defaultStamina);
            cache.put(uuid, data);
        } else if (setIfCached) {
            data.setMana(defaultMana);
            data.setStamina(defaultStamina);
        }
        return data;
    }

    public void save(ResourceData data) {
        UUID uuid = data.getUniqueId();
        if (!cache.containsKey(uuid)) {
            data.unregisterModifiers();
        }
        String name = Util.getOfflinePlayer(uuid).map(OfflinePlayer::getName).orElse(null);
        double mana = data.getMana();
        double stamina = data.getStamina();
        try (Connection conn = plugin.getConnection()) {
            save(conn, uuid, name == null ? "" : name, mana, stamina);
        } catch (SQLException e) {
            warn(e);
        }
    }

    private void save(Connection conn, UUID uuid, String name, double mana, double stamina) throws SQLException {
        DatabaseHolder holder = plugin.options.database();
        if (holder.isSQLite()) {
            try (PreparedStatement ps = conn.prepareStatement(
                         "INSERT OR REPLACE INTO `" + table + "`(`uuid`,`name`,`mana`,`stamina`) VALUES(?, ?, ?, ?);"
                 )) {
                ps.setString(1, uuid.toString());
                ps.setString(2, name);
                ps.setDouble(3, mana);
                ps.setDouble(4, stamina);
                ps.execute();
            }
        } else if (holder.isMySQL()) {
            try (PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO `" + table + "`(`uuid`,`name`,`mana`,`stamina`) VALUES(?, ?, ?, ?) on duplicate key update `name`=?, `mana`=?, `stamina`=?;"
                 )) {
                ps.setString(1, uuid.toString());
                ps.setString(2, name);
                ps.setDouble(3, mana);
                ps.setDouble(4, stamina);
                ps.setString(5, name);
                ps.setDouble(6, mana);
                ps.setDouble(7, stamina);
                ps.execute();
            }
        }
    }
}
