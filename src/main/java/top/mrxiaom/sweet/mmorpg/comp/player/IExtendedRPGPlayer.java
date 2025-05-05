package top.mrxiaom.sweet.mmorpg.comp.player;

import top.mrxiaom.sweet.mmorpg.api.ResourceData;
import top.mrxiaom.sweet.mmorpg.api.StatType;

public interface IExtendedRPGPlayer {
    ResourceData getData();
    default double getMaxMana() {
        return getData().getStat(StatType.MAX_MANA);
    }
    default double getMaxStamina() {
        return getData().getStat(StatType.MAX_STAMINA);
    }
}
