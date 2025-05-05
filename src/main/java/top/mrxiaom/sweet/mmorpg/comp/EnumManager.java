package top.mrxiaom.sweet.mmorpg.comp;

public enum EnumManager {
    BuiltIn(true, true),
    AuraSkills(false, true)

    ;
    public final boolean regenMana, regenStamina;
    EnumManager(boolean regenMana, boolean regenStamina) {
        this.regenMana = regenMana;
        this.regenStamina = regenStamina;
    }
}
