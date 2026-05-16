package org.magicteam.relicsandfoes.init;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.world.item.PortableAncientRelicTeleporterItem;

public final class RAFItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RelicsAndFoes.MODID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(RelicsAndFoes.MODID);

    public static final DeferredItem<PortableAncientRelicTeleporterItem> PORTABLE_ANCIENT_RELIC_TELEPORTER = ITEMS.register("portable_ancient_relic_teleporter", () -> new PortableAncientRelicTeleporterItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
    }
}
