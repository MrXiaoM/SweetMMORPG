package top.mrxiaom.sweet.mmorpg.comp.player;

import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import top.mrxiaom.sweet.mmorpg.SweetMMORPG;
import top.mrxiaom.sweet.mmorpg.api.ResourceData;

import java.util.UUID;

public class PlayerBuiltIn extends RPGPlayer implements IExtendedRPGPlayer {
    private final SweetMMORPG plugin;
    private final ResourceData data;

    public PlayerBuiltIn(SweetMMORPG plugin, PlayerData playerData) {
        super(playerData);
        UUID uuid = playerData.getUniqueId();
        this.plugin = plugin;
        this.data = plugin.getPlayerDatabase().getOrCached(uuid).setPlayer(this);
    }

    @Override
    public ResourceData getData() {
        return data;
    }

    @Override
    public int getLevel() {
        return getPlayer().getLevel();
    }

    @Override
    public String getClassName() {
        return "";
    }

    @Override
    public double getMana() {
        return data.getMana();
    }

    @Override
    public double getStamina() {
        return data.getStamina();
    }

    @Override
    public void setMana(double value) {
        data.setMana(value);
    }

    @Override
    public void setStamina(double value) {
        data.setStamina(value);
    }

    @Override
    public void giveMana(double value) {
        data.giveMana(value);
    }

    @Override
    public void giveStamina(double value) {
        data.giveStamina(value);
    }
}
