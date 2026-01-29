package top.mrxiaom.sweet.mmorpg.comp;

import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import top.mrxiaom.pluginbase.utils.Util;

import java.util.function.Consumer;
import java.util.function.Function;

@ApiStatus.Internal
public class MMOItemsCompatibility {
    private final Consumer<Player> consumer;
    private final Runnable setRPGHandler;
    protected MMOItemsCompatibility(JavaPlugin plugin, Listener listener, Function<PlayerData, RPGPlayer> getInfo) {
        if (Util.isPresent("net.Indyuce.mmoitems.api.player.inventory.InventoryUpdateHandler")) {
            v6_10 mmoitems = new v6_10(plugin, listener);
            consumer = mmoitems::resolveLevelChange;
            setRPGHandler = mmoitems::setRPG;
            mmoitems.setGetInfo(getInfo);
        } else {
            v6_10_1 mmoitems = new v6_10_1(plugin, listener);
            consumer = mmoitems::resolveLevelChange;
            setRPGHandler = mmoitems::setRPG;
            mmoitems.setGetInfo(getInfo);
        }
    }

    public void setRPG() {
        setRPGHandler.run();
    }

    public void resolveLevelChange(Player player) {
        consumer.accept(player);
    }
}
