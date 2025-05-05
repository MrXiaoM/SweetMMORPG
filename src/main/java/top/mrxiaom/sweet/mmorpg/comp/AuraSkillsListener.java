package top.mrxiaom.sweet.mmorpg.comp;

import dev.aurelium.auraskills.api.event.user.UserLoadEvent;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
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
        PlayerData data = PlayerData.get(e.getPlayer().getUniqueId());
        RPGPlayer rpg = data.getRPG();
        if (rpg instanceof PlayerAuraSkills) {
            ((PlayerAuraSkills) rpg).setUser(e.getUser());
        }
    }
}
