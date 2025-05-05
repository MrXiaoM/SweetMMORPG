package top.mrxiaom.sweet.mmorpg;
        
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.comp.rpg.RPGHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import top.mrxiaom.pluginbase.BukkitPlugin;
import top.mrxiaom.sweet.mmorpg.api.StatType;
import top.mrxiaom.sweet.mmorpg.comp.MMOHook;
import top.mrxiaom.sweet.mmorpg.comp.Placeholders;
import top.mrxiaom.sweet.mmorpg.database.PlayerDatabase;

import java.util.logging.Level;

public class SweetMMORPG extends BukkitPlugin {
    public static SweetMMORPG getInstance() {
        return (SweetMMORPG) BukkitPlugin.getInstance();
    }

    public SweetMMORPG() {
        super(options()
                .bungee(false)
                .adventure(false)
                .database(true)
                .reconnectDatabaseWhenReloadConfig(false)
                .scanIgnore("top.mrxiaom.sweet.mmorpg.libs")
        );
    }
    private PlayerDatabase playerDatabase;

    public PlayerDatabase getPlayerDatabase() {
        return playerDatabase;
    }

    @Override
    protected void beforeLoad() {
        for (StatType type : StatType.values())
            type.registerStat();
    }

    @Override
    protected void beforeEnable() {
        options.registerDatabase(
                playerDatabase = new PlayerDatabase(this)
        );
    }

    @Override
    protected void afterEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholders(this).register();
        }
        MMOHook handler = new MMOHook(this);
        try {
            // https://gitlab.com/phoenix-dvpmt/mmoitems/-/issues/1699
            MMOItems.plugin.setRPG(handler);
        } catch (java.lang.ClassCastException e) {
            // 备用方案
            RPGHandler oldRPG = MMOItems.plugin.getMainRPG();
            if (MMOItems.plugin.isEnabled()) {
                if (oldRPG instanceof Plugin) {
                    HandlerList.unregisterAll((Plugin) oldRPG);
                }
                if (oldRPG instanceof Listener) {
                    HandlerList.unregisterAll((Listener) oldRPG);
                }
            }
            MMOItems.plugin.getRPGs().add(0, handler);
            MMOItems.plugin.getLogger().log(Level.INFO, "Now using " + handler.getClass().getSimpleName() + " as RPG provider");
            if (MMOItems.plugin.isEnabled()) {
                Bukkit.getPluginManager().registerEvents(handler, this);
            }
        }
        Bukkit.getOnlinePlayers().forEach(playerDatabase::load);
        getLogger().info("SweetMMORPG 加载完毕");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
    }
}
