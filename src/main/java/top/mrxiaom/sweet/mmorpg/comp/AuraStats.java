package top.mrxiaom.sweet.mmorpg.comp;

import dev.aurelium.auraskills.api.item.ItemContext;
import dev.aurelium.auraskills.api.registry.NamespacedId;
import dev.aurelium.auraskills.api.stat.CustomStat;

public class AuraStats {
    public static final CustomStat STAMINA = CustomStat
            .builder(NamespacedId.of("sweetmmorpg", "stamina"))
                    .displayName("Stamina")
                    .description("The stamina value from SweetMMORPG with MMOItems.")
                    .color("<green>")
                    .symbol("")
                    .item(ItemContext.builder()
                            .material("slime_ball")
                            .group("lower")
                            .build())
                    .build();
}
