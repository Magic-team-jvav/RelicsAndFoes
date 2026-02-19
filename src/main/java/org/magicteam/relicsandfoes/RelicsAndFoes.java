package org.magicteam.relicsandfoes;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.magicteam.relicsandfoes.init.ModBlocks;
import org.magicteam.relicsandfoes.init.ModDimensions;
import org.magicteam.relicsandfoes.init.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(RelicsAndFoes.MODID)
public class RelicsAndFoes {
    public static final String MODID = "relics_and_foes";
    public static final Logger LOGGER = LoggerFactory.getLogger("RelicsAndFoes");

    public RelicsAndFoes(IEventBus eventBus, ModContainer container) {
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModDimensions.register(eventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
