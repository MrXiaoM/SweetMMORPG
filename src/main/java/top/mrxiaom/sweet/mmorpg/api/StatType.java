package top.mrxiaom.sweet.mmorpg.api;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import org.bukkit.configuration.ConfigurationSection;
import top.mrxiaom.sweet.mmorpg.comp.EnumManager;
import top.mrxiaom.sweet.mmorpg.stat.ManaRegeneration;
import top.mrxiaom.sweet.mmorpg.stat.MaxStamina;
import top.mrxiaom.sweet.mmorpg.stat.StaminaRegeneration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum StatType {
    MAX_MANA(null),
    MAX_STAMINA(new MaxStamina()),
    MANA_REGENERATION(new ManaRegeneration()),
    STAMINA_REGENERATION(new StaminaRegeneration());

    private static final Map<StatType, Function<ResourceData, Double>> baseValues = new HashMap<>();
    private final ItemStat<?, ?> stat;

    StatType(ItemStat<?, ?> stat) {
        this.stat = stat;
    }

    public double getBase(ResourceData data) {
        Function<ResourceData, Double> func = baseValues.getOrDefault(this, null);
        return func == null ? 0.0 : func.apply(data);
    }

    public void registerStat() {
        if (stat != null)
            MMOItems.plugin.getStats().register(stat);
    }

    public static void reloadConfig(EnumManager manager, ConfigurationSection config) {
        baseValues.clear();
        for (StatType type : values()) {
            String key = "default." + type.name().toLowerCase().replace("_", "-");
            double defaultValue = config.getDouble(key);
            baseValues.put(type, data -> defaultValue);
        }
    }
}