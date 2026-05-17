package org.magicteam.relicsandfoes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.magicteam.relicsandfoes.init.*;
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
        RAFDataComponents.TYPES.register(eventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static void generator(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        RAFItems.ITEMS.getEntries().forEach(holder -> output.accept(holder.get()));
        RAFItems.BLOCK_ITEMS.getEntries().forEach(holder -> output.accept(holder.get()));
    }
}
