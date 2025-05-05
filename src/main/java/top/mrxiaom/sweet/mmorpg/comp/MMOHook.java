package top.mrxiaom.sweet.mmorpg.comp;

import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.Indyuce.mmoitems.comp.rpg.RPGHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import top.mrxiaom.sweet.mmorpg.SweetMMORPG;
import top.mrxiaom.sweet.mmorpg.comp.player.PlayerAuraSkills;
import top.mrxiaom.sweet.mmorpg.comp.player.PlayerBuiltIn;
import top.mrxiaom.sweet.mmorpg.func.AbstractPluginHolder;

public class MMOHook extends AbstractPluginHolder implements RPGHandler, Listener {
    LevelChangeResolver levelChange = new LevelChangeResolver();
    public MMOHook(SweetMMORPG plugin) {
        super(plugin, true);
        if (plugin.getManagerType().equals(EnumManager.AuraSkills)) {
            new AuraSkillsListener(plugin);
        }
    }

    @EventHandler
    public void a(PlayerLevelChangeEvent event) {
        levelChange.resolve(event.getPlayer());
    }

    public RPGPlayer getInfo(PlayerData data) {
        EnumManager manager = plugin.getManagerType();
        switch (manager) {
            case BuiltIn:
                return new PlayerBuiltIn(plugin, data);
            case AuraSkills:
                return new PlayerAuraSkills(plugin, data);
        }
        throw new IllegalStateException("预料之中的错误: 未知的管理器类型 " + manager.name());
    }

    @Override
    public void refreshStats(PlayerData data) {
    }

    public static MMOHook inst() {
        return instanceOf(MMOHook.class);
    }
}
