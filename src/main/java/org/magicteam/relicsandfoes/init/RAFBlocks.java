package org.magicteam.relicsandfoes.init;

import com.mojang.datafixers.DSL;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.block.AncientPortalBlock;

import java.util.function.Function;
import java.util.function.Supplier;

public final class RAFBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RelicsAndFoes.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RelicsAndFoes.MODID);

    public static final DeferredBlock<AncientPortalBlock> ANCIENT_PORTAL = registerWithItem("ancient_portal", AncientPortalBlock::new, AncientPortalBlock.BItem::new);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AncientPortalBlock.BEntity>> ANCIENT_PORTAL_ENTITY = BLOCK_ENTITIES.register("ancient_portal", () -> BlockEntityType.Builder.of(AncientPortalBlock.BEntity::new, ANCIENT_PORTAL.get()).build(DSL.remainderType()));

    public static final DeferredBlock<Block> ANCIENT_SPAWNER = registerWithItem("ancient_spawner", () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> CRYSTAL_SKULL = registerWithItem("crystal_skull", () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> SPAWN_ROBOT = registerWithItem("spawn_robot", () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> SPAWN_ATTACK_ROBOT = registerWithItem("spawn_attack_robot", () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> SPAWN_DEFENSE_TOWER = registerWithItem("spawn_defense_tower", () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> SPAWN_AIRCRAFT = registerWithItem("spawn_aircraft", () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> SPAWN_PRIEST = registerWithItem("spawn_priest", () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> SPAWN_PROTOTYPE = registerWithItem("spawn_prototype", () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> PROTOTYPE_CHEST = registerWithItem("prototype_chest", () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> CONDUCTOR_CHEST = registerWithItem("conductor_chest", () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> KYLIN_CHEST = registerWithItem("kylin_chest", () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> ANGEL_CHEST = registerWithItem("angel_chest", () -> new Block(BlockBehaviour.Properties.of()));

    private static <B extends Block> DeferredBlock<B> registerWithItem(String name, Supplier<B> block, Function<B, BlockItem> item) {
        DeferredBlock<B> holder = BLOCKS.register(name, block);
        RAFItems.BLOCK_ITEMS.register(name, () -> item.apply(holder.get()));
        return holder;
    }

    private static <B extends Block> DeferredBlock<B> registerWithItem(String name, Supplier<B> block) {
        return registerWithItem(name, block, b -> new BlockItem(b, new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
