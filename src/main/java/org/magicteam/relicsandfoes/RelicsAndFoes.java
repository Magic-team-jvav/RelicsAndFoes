package org.magicteam.relicsandfoes;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
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
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
