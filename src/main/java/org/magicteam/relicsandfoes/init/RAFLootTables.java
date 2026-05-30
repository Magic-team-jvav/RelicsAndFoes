package org.magicteam.relicsandfoes.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import org.magicteam.relicsandfoes.RelicsAndFoes;

public final class RAFLootTables {
    public static final ResourceKey<LootTable> CRAR_PILGRIMAGE_EYE = of("cross_realm_ancient_ruins/pilgrimage_eye"); // "chests/pilgrimage_eye"
    public static final ResourceKey<LootTable> CRAR_PILGRIMAGE_ITEM = of("cross_realm_ancient_ruins/pilgrimage_item"); // "chests/pilgrimage_item"
    public static final ResourceKey<LootTable> CRAR_COMMON = of("cross_realm_ancient_ruins/common"); // "chests/ruin_city"

    public static final ResourceKey<LootTable> TPBV_MOUNTAIN_SMALL = of("the_peach_blossom_vale/mountain_small"); // "chests/mountain_small"

    private static ResourceKey<LootTable> of(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, RelicsAndFoes.asResource(path));
    }
}
