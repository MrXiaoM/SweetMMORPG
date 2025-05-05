package top.mrxiaom.sweet.mmorpg.comp;

import io.lumine.mythic.lib.MythicLib;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.sweet.mmorpg.SweetMMORPG;
import top.mrxiaom.sweet.mmorpg.api.ResourceData;
import top.mrxiaom.sweet.mmorpg.api.StatType;
import top.mrxiaom.sweet.mmorpg.comp.player.IExtendedRPGPlayer;

public class Placeholders extends PlaceholderExpansion {
    SweetMMORPG plugin;
    public Placeholders(SweetMMORPG plugin) {
        this.plugin = plugin;
    }
    @Override
    public @NotNull String getIdentifier() {
        return plugin.getDescription().getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        ResourceData data = plugin.getPlayerDatabase().getOrCached(player);
        RPGPlayer rpg = data == null ? null : data.getPlayer();
        if (data == null || rpg == null) return "null";
        switch (params.toLowerCase()) {
            case "mana":
                return MythicLib.plugin.getMMOConfig().decimal.format(rpg.getMana());
            case "stamina":
                return MythicLib.plugin.getMMOConfig().decimal.format(rpg.getStamina());
            case "max_mana":
                if (rpg instanceof IExtendedRPGPlayer) {
                    return MythicLib.plugin.getMMOConfig().decimal.format(((IExtendedRPGPlayer) rpg).getMaxMana());
                } else {
                    return MythicLib.plugin.getMMOConfig().decimal.format(data.getStat(StatType.MAX_MANA));
                }
            case "max_stamina":
                if (rpg instanceof IExtendedRPGPlayer) {
                    return MythicLib.plugin.getMMOConfig().decimal.format(((IExtendedRPGPlayer) rpg).getMaxStamina());
                } else {
                    return MythicLib.plugin.getMMOConfig().decimal.format(data.getStat(StatType.MAX_STAMINA));
                }
            case "mana_regen":
                return MythicLib.plugin.getMMOConfig().decimal.format(data.getStat(StatType.MANA_REGENERATION));
            case "stamina_regen":
                return MythicLib.plugin.getMMOConfig().decimal.format(data.getStat(StatType.STAMINA_REGENERATION));
            default:
                return null;
        }
    }
}
