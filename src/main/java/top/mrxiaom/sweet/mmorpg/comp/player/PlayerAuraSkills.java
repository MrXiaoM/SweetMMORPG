package top.mrxiaom.sweet.mmorpg.comp.player;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.user.SkillsUser;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import top.mrxiaom.sweet.mmorpg.SweetMMORPG;
import top.mrxiaom.sweet.mmorpg.api.ResourceData;

import java.util.UUID;

public class PlayerAuraSkills extends RPGPlayer implements IExtendedRPGPlayer {
    private final SweetMMORPG plugin;
    private final ResourceData data;
    private final SkillsUser user;

    public PlayerAuraSkills(SweetMMORPG plugin, PlayerData playerData) {
        super(playerData);
        UUID uuid = playerData.getUniqueId();
        this.plugin = plugin;
        this.data = plugin.getPlayerDatabase().getOrCached(uuid).setPlayer(this);
        this.user = AuraSkillsApi.get().getUser(uuid);
    }

    @Override
    public ResourceData getData() {
        return data;
    }

    @Override
    public int getLevel() {
        return user.getPowerLevel();
    }

    @Override
    public String getClassName() {
        return "";
    }

    @Override
    public double getMana() {
        return user.getMana();
    }

    @Override
    public double getMaxMana() {
        return user.getMaxMana();
    }

    @Override
    public double getStamina() {
        return data.getStamina();
    }

    @Override
    public void setMana(double value) {
        user.setMana(value);
    }

    @Override
    public void setStamina(double value) {
        data.setStamina(value);
    }

    @Override
    public void giveMana(double value) {
        double max = user.getMaxMana();
        double mana = user.getMana();
        user.setMana(Math.max(max, mana + value));
    }

    @Override
    public void giveStamina(double value) {
        data.giveStamina(value);
    }
}
