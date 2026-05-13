package org.magicteam.relicsandfoes.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;

public final class RAFItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RelicsAndFoes.MODID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(RelicsAndFoes.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
    }
}
