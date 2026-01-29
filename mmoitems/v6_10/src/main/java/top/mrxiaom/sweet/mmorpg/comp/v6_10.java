package top.mrxiaom.sweet.mmorpg.comp;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.Indyuce.mmoitems.comp.rpg.RPGHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;
import java.util.logging.Level;

@ApiStatus.Internal
public class v6_10 implements RPGHandler {
    private final JavaPlugin plugin;
    private final Listener listener;
    private Function<PlayerData, RPGPlayer> getInfo;
    public v6_10(JavaPlugin plugin, Listener listener) {
        this.plugin = plugin;
        this.listener = listener;
    }

    public void resolveLevelChange(Player player) {
        PlayerData.get(player).getInventory().scheduleUpdate();
    }

    public void setRPG() {
        try {
            // https://gitlab.com/phoenix-dvpmt/mmoitems/-/issues/1699
            MMOItems.plugin.setRPG(this);
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
            MMOItems.plugin.getRPGs().add(0, this);
            MMOItems.plugin.getLogger().log(Level.INFO, "Now using " + getClass().getSimpleName() + " as RPG provider");
            if (MMOItems.plugin.isEnabled()) {
                Bukkit.getPluginManager().registerEvents(listener, plugin);
            }
        }
    }

    public void setGetInfo(Function<PlayerData, RPGPlayer> getInfo) {
        this.getInfo = getInfo;
    }

    @Override
    public RPGPlayer getInfo(PlayerData playerData) {
        return getInfo == null ? null : getInfo.apply(playerData);
    }

    @Override
    public void refreshStats(PlayerData playerData) {

    }
}
