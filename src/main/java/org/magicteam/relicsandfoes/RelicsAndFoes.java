package org.magicteam.relicsandfoes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.magicteam.relicsandfoes.init.RAFBlocks;
import org.magicteam.relicsandfoes.init.RAFDimensions;
import org.magicteam.relicsandfoes.init.RAFItems;
import org.magicteam.relicsandfoes.init.RAFSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(RelicsAndFoes.MODID)
public final class RelicsAndFoes {
    public static final String MODID = "relics_and_foes";
    public static final Logger LOGGER = LoggerFactory.getLogger("RelicsAndFoes");

    public RelicsAndFoes(IEventBus eventBus, ModContainer container) {
        RAFBlocks.register(eventBus);
        RAFItems.register(eventBus);
        RAFDimensions.register(eventBus);
        RAFSounds.SOUND_EVENTS.register(eventBus);

        if (!FMLEnvironment.production) {
            addBlockAlias("ancient_portal", RAFBlocks.ANCIENT_PORTAL.getId());
            addBlockAlias("ancient_spawner", RAFBlocks.ANCIENT_SPAWNER.getId());
            addBlockAlias("crystal_skull_block", RAFBlocks.CRYSTAL_SKULL.getId());
            addBlockAlias("spawn_robot", RAFBlocks.SPAWN_ROBOT.getId());
            addBlockAlias("spawn_attack_robot", RAFBlocks.SPAWN_ATTACK_ROBOT.getId());
            addBlockAlias("spawn_defense_tower", RAFBlocks.SPAWN_DEFENSE_TOWER.getId());
            addBlockAlias("spawn_aircraft", RAFBlocks.SPAWN_AIRCRAFT.getId());
            addBlockAlias("spawn_priest", RAFBlocks.SPAWN_PRIEST.getId());
            addBlockAlias("spawn_prototype", RAFBlocks.SPAWN_PROTOTYPE.getId());
            addBlockAlias("prototype_chest", RAFBlocks.PROTOTYPE_CHEST.getId());
            addBlockAlias("conductor_chest", RAFBlocks.CONDUCTOR_CHEST.getId());
            addBlockAlias("kylin_chest", RAFBlocks.KYLIN_CHEST.getId());
            addBlockAlias("angel_chest", RAFBlocks.ANGEL_CHEST.getId());
        }
    }

    private static void addBlockAlias(String from, ResourceLocation to) {
        BuiltInRegistries.BLOCK.addAlias(ResourceLocation.fromNamespaceAndPath("cataclysm", from), to);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
