package top.mrxiaom.sweet.mmorpg.func;

import org.bukkit.configuration.MemoryConfiguration;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.mmorpg.SweetMMORPG;
import top.mrxiaom.sweet.mmorpg.api.StatType;

@AutoRegister
public class StatDefaultDataManager extends AbstractModule {
    public StatDefaultDataManager(SweetMMORPG plugin) {
        super(plugin);
    }

    @Override
    public int priority() {
        return 999;
    }

    @Override
    public void reloadConfig(MemoryConfiguration config) {
        StatType.reloadConfig(plugin.getManagerType(), config);
    }
}
