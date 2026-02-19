package org.magicteam.relicsandfoes.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.block.AncientPortalBlock;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RelicsAndFoes.MODID);

    public static final DeferredBlock<AncientPortalBlock> ANCIENT_PORTAL = registerWithItem("ancient_portal", AncientPortalBlock::new);

    private static <B extends Block> DeferredBlock<B> registerWithItem(String name, Supplier<B> block, Function<B, BlockItem> item) {
        DeferredBlock<B> holder = BLOCKS.register(name, block);
        ModItems.BLOCK_ITEMS.register(name, () -> item.apply(holder.get()));
        return holder;
    }

    private static <B extends Block> DeferredBlock<B> registerWithItem(String name, Supplier<B> block) {
        return registerWithItem(name, block, b -> new BlockItem(b, new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
