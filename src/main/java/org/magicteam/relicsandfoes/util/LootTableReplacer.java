package org.magicteam.relicsandfoes.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.RAFLootTables;

public class LootTableReplacer {
    private static final ResourceKey<LootTable> PILGRIMAGE_EYE = original("chests/pilgrimage_eye");
    private static final ResourceKey<LootTable> PILGRIMAGE_ITEM = original("chests/pilgrimage_item");
    private static final ResourceKey<LootTable> RUINS_CITY = original("chests/ruins_city");

    private static ResourceKey<LootTable> original(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace(path));
    }

    public static void replace(ServerLevel level, BlockPos... positions) {
        for (BlockPos pos : positions) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RandomizableContainer container) {
                ResourceKey<LootTable> key = container.getLootTable();
                if (key == PILGRIMAGE_EYE) {
                    replace(container, RAFLootTables.CRAR_PILGRIMAGE_EYE);
                } else if (key == PILGRIMAGE_ITEM) {
                    replace(container, RAFLootTables.CRAR_PILGRIMAGE_ITEM);
                } else if (key == RUINS_CITY) {
                    replace(container, RAFLootTables.CRAR_COMMON);
                } else {
                    RelicsAndFoes.LOGGER.warn("[LootTableReplacer] unknown loot table: {} at [{}]", key == null ? null : key.location(), pos.toShortString());
                }
            } else {
                RelicsAndFoes.LOGGER.warn("[LootTableReplacer] block entity not found at: [{}]", pos.toShortString());
            }
        }
    }

    private static void replace(RandomizableContainer container, ResourceKey<LootTable> key) {
        container.setLootTable(key);
        container.setChanged();
        RelicsAndFoes.LOGGER.debug("[LootTableReplacer] {} set at [{}]!", key.location(), container.getBlockPos().toShortString());
    }
}
