package top.mrxiaom.sweet.mmorpg.comp;

import dev.aurelium.auraskills.api.event.user.UserLoadEvent;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.Indyuce.mmoitems.manager.data.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.mmorpg.SweetMMORPG;
import top.mrxiaom.sweet.mmorpg.comp.player.PlayerAuraSkills;
import top.mrxiaom.sweet.mmorpg.func.AbstractModule;

@AutoRegister(requirePlugins = "AuraSkills")
public class AuraSkillsListener extends AbstractModule implements Listener {
    public AuraSkillsListener(SweetMMORPG plugin) {
        super(plugin);
        registerEvents();
    }
    @EventHandler
    public void onUserLoad(UserLoadEvent e) {
        PlayerData data = getOrSetup(e.getPlayer());
        RPGPlayer rpg = data.getRPG();
        if (rpg instanceof PlayerAuraSkills) {
            ((PlayerAuraSkills) rpg).setUser(e.getUser());
        }
    }

    private static PlayerData getOrSetup(Player player) {
        PlayerDataManager manager = MMOItems.plugin.getPlayerDataManager();
        PlayerData data = manager.getOrNull(player);
        if (data != null) {
            return data;
        }
        return manager.setup(player);
    }
}
