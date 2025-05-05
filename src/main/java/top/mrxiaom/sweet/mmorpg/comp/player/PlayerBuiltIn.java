package top.mrxiaom.sweet.mmorpg.comp.player;

import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import top.mrxiaom.sweet.mmorpg.SweetMMORPG;
import top.mrxiaom.sweet.mmorpg.api.ResourceData;

public class PlayerBuiltIn extends RPGPlayer {
    private final SweetMMORPG plugin;
    private final ResourceData data;

    public PlayerBuiltIn(SweetMMORPG plugin, PlayerData playerData) {
        super(playerData);
        this.plugin = plugin;
        this.data = plugin.getPlayerDatabase().getOrCached(playerData.getUniqueId());
    }

    public int getLevel() {
        return getPlayer().getLevel();
    }

    public String getClassName() {
        return "";
    }

    public double getMana() {
        return data.getMana();
    }

    public double getStamina() {
        return data.getStamina();
    }

    public void setMana(double value) {
        data.setMana(value);
    }

    public void setStamina(double value) {
        data.setStamina(value);
    }

    public void giveMana(double value) {
        data.giveMana(value);
    }

    public void giveStamina(double value) {
        data.giveStamina(value);
    }
}
