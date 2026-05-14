package org.magicteam.relicsandfoes.init;

import com.mojang.datafixers.DSL;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.world.block.crossrealmancientruins.AncientRelicTeleporterBlock;
import org.magicteam.relicsandfoes.world.block.thesunkenexpanse.HangingVineBodyBlock;
import org.magicteam.relicsandfoes.world.block.thesunkenexpanse.HangingVineHeadBlock;
import org.magicteam.relicsandfoes.world.block.thesunkenexpanse.WildfieldCornBlock;
import org.magicteam.relicsandfoes.world.block.thesunkenexpanse.WildfieldCornStalkBlock;

import java.util.function.Function;
import java.util.function.Supplier;

public final class RAFBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RelicsAndFoes.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RelicsAndFoes.MODID);

    // region 古迹大陆 Relic Land

    public static final DeferredBlock<TallGrassBlock> ANCIENT_WILDGRASS = BLOCKS.register("ancient_wildgrass", () -> new TallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS))); // 古迹野草
    public static final DeferredBlock<DoublePlantBlock> ANCIENT_TALL_WILDGRASS = BLOCKS.register("ancient_tall_wildgrass", () -> new DoublePlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS))); // 古迹野草高变种

    // endregion

    // region 通界古墟 Cross-Realm Ancient Ruins

    public static final DeferredBlock<AncientRelicTeleporterBlock> ANCIENT_RELIC_TELEPORTER = registerWithItem("ancient_relic_teleporter", AncientRelicTeleporterBlock::new, AncientRelicTeleporterBlock.BItem::new); // 古迹传送台
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AncientRelicTeleporterBlock.BEntity>> ANCIENT_RELIC_TELEPORTER_ENTITY = BLOCK_ENTITIES.register("ancient_relic_teleporter", () -> BlockEntityType.Builder.of(AncientRelicTeleporterBlock.BEntity::new, ANCIENT_RELIC_TELEPORTER.get()).build(DSL.remainderType()));
    public static final DeferredBlock<Block> ANCIENT_RELIC_RESPAWN_PLATFORM = registerWithItem("ancient_relic_respawn_platform", () -> new Block(BlockBehaviour.Properties.of())); // 古迹重生台

    // endregion

    // region 沉沦原野 The Sunken Expanse

    public static final DeferredBlock<WildfieldCornBlock> WILDFIELD_CORN = registerWithItem("wildfield_corn", () -> new WildfieldCornBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TORCHFLOWER))); // 原野玉米
    public static final DeferredBlock<WildfieldCornStalkBlock> WILDFIELD_CORN_STALK = registerWithItem("wildfield_corn_stalk", () -> new WildfieldCornStalkBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TORCHFLOWER))); // 原野玉米茎
    public static final DeferredBlock<TallGrassBlock> WILDFIELD_WEEDS = registerWithItem("wildfield_weeds", () -> new TallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS))); // 原野杂草
    public static final DeferredBlock<TallGrassBlock> FALLEN_LAVENDER = registerWithItem("fallen_lavender", () -> new TallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS))); // 沉沦薰衣草
    public static final DeferredBlock<Block> FALLEN_COFFER = registerWithItem("fallen_coffer", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL))); // 沉沦宝匣
    public static final DeferredBlock<TallGrassBlock> AGAVE = registerWithItem("agave", () -> new TallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS))); // 龙舌兰
    public static final DeferredBlock<HangingVineHeadBlock> HANGING_VINE_HEAD = registerWithItem("hanging_vine_head", () -> new HangingVineHeadBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).randomTicks().noCollission().instabreak().sound(SoundType.VINE).pushReaction(PushReaction.DESTROY))); // 垂挂树藤头
    public static final DeferredBlock<HangingVineBodyBlock> HANGING_VINE_BODY = registerWithItem("hanging_vine_body", () -> new HangingVineBodyBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).randomTicks().noCollission().instabreak().sound(SoundType.VINE).pushReaction(PushReaction.DESTROY))); // 垂挂树藤体

    // endregion

    // region 占位符

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

    // endregion

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

        if (!FMLEnvironment.production) {
            addBlockAlias("cataclysm:ancient_portal", ANCIENT_RELIC_TELEPORTER.getId());
            addBlockAlias("relics_and_foes:ancient_portal", ANCIENT_RELIC_TELEPORTER.getId());
            addBlockEntityAlias("relics_and_foes:ancient_portal", ANCIENT_RELIC_TELEPORTER_ENTITY.getId());
            addBlockAlias("cataclysm:ancient_spawner", ANCIENT_RELIC_RESPAWN_PLATFORM.getId());
            addBlockAlias("relics_and_foes:ancient_spawner", ANCIENT_RELIC_RESPAWN_PLATFORM.getId());
            addBlockAlias("cataclysm:crystal_skull_block", CRYSTAL_SKULL.getId());
            addBlockAlias("cataclysm:spawn_robot", SPAWN_ROBOT.getId());
            addBlockAlias("cataclysm:spawn_attack_robot", SPAWN_ATTACK_ROBOT.getId());
            addBlockAlias("cataclysm:spawn_defense_tower", SPAWN_DEFENSE_TOWER.getId());
            addBlockAlias("cataclysm:spawn_aircraft", SPAWN_AIRCRAFT.getId());
            addBlockAlias("cataclysm:spawn_priest", SPAWN_PRIEST.getId());
            addBlockAlias("cataclysm:spawn_prototype", SPAWN_PROTOTYPE.getId());
            addBlockAlias("cataclysm:prototype_chest", PROTOTYPE_CHEST.getId());
            addBlockAlias("cataclysm:conductor_chest", CONDUCTOR_CHEST.getId());
            addBlockAlias("cataclysm:kylin_chest", KYLIN_CHEST.getId());
            addBlockAlias("cataclysm:angel_chest", ANGEL_CHEST.getId());
        }
    }

    private static void addBlockAlias(String from, ResourceLocation to) {
        BuiltInRegistries.BLOCK.addAlias(ResourceLocation.parse(from), to);
    }

    private static void addBlockEntityAlias(String from, ResourceLocation to) {
        BuiltInRegistries.BLOCK_ENTITY_TYPE.addAlias(ResourceLocation.parse(from), to);
    }
}
