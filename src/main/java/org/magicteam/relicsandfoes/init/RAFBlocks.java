package org.magicteam.relicsandfoes.init;

import com.mojang.datafixers.DSL;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.world.block.*;

import java.util.function.Function;
import java.util.function.Supplier;

public final class RAFBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RelicsAndFoes.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RelicsAndFoes.MODID);

    // region 古迹大陆 Relic Land

    public static final DeferredBlock<MoveShapeTallGrassBlock> ANCIENT_WILDGRASS = registerWithItem("ancient_wildgrass", () -> new MoveShapeTallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS))); // 古迹野草
    public static final DeferredBlock<DoublePlantBlock> ANCIENT_TALL_WILDGRASS = registerWithItem("ancient_tall_wildgrass", () -> new DoublePlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS))); // 古迹野草高变种
    public static final DeferredBlock<AbandonedStaffBlock> ABANDONED_TABLET = registerWithItem("abandoned_tablet", () -> new AbandonedStaffBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE), Block.box(1, 0, 1, 15, 16, 15)));
    public static final DeferredBlock<AbandonedStaffBlock> ABANDONED_PLANKS = registerWithItem("abandoned_planks", () -> new AbandonedStaffBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS), Block.box(1, 0, 1, 15, 4, 15)));
    public static final DeferredBlock<AbandonedStaffBlock> ABANDONED_SIGN = registerWithItem("abandoned_sign", () -> new AbandonedStaffBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_SIGN), Block.box(5, 0, 5, 11, 16, 11)));
    public static final DeferredBlock<Block> COVERED_GRASS_STONE = registerWithItem("covered_grass_stone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).mapColor(MapColor.GRASS)));
    public static final DeferredBlock<ThicketBlock> ANCIENT_THICKET = registerWithItem("ancient_thicket", () -> new ThicketBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_LEAVES))); // 古迹草丛
    public static final DeferredBlock<ForestVineBlock> FOREST_VINE = registerWithItem("forest_vine", () -> new ForestVineBlock(noRandomTickVineProperties())); // 森林藤蔓

    // endregion 古迹大陆

    // region 沉沦原野 The Sunken Expanse

    // region 通界古墟 Cross-Realm Ancient Ruins

    public static final DeferredBlock<AncientRelicTeleporterBlock> ANCIENT_RELIC_TELEPORTER = registerWithItem("ancient_relic_teleporter", AncientRelicTeleporterBlock::new, AncientRelicTeleporterBlock.BItem::new); // 古迹传送台
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AncientRelicTeleporterBlock.BEntity>> ANCIENT_RELIC_TELEPORTER_ENTITY = BLOCK_ENTITIES.register("ancient_relic_teleporter", () -> BlockEntityType.Builder.of(AncientRelicTeleporterBlock.BEntity::new, ANCIENT_RELIC_TELEPORTER.get()).build(DSL.remainderType()));
    public static final DeferredBlock<Block> ANCIENT_RELIC_RESPAWN_PLATFORM = registerWithItem("ancient_relic_respawn_platform", () -> new Block(BlockBehaviour.Properties.of())); // 古迹重生台

    // endregion 通界古墟

    public static final DeferredBlock<StalkBlockHead> WILDFIELD_CORN = registerWithItem("wildfield_corn", () -> new StalkBlockHead(BlockBehaviour.Properties.ofFullCopy(Blocks.TORCHFLOWER), RAFBlocks.WILDFIELD_CORN_STALK)); // 原野玉米
    public static final DeferredBlock<StalkBlockStem> WILDFIELD_CORN_STALK = registerWithItem("wildfield_corn_stalk", () -> new StalkBlockStem(BlockBehaviour.Properties.ofFullCopy(Blocks.TORCHFLOWER))); // 原野玉米茎
    public static final DeferredBlock<MoveShapeTallGrassBlock> WILDFIELD_WEEDS = registerWithItem("wildfield_weeds", () -> new MoveShapeTallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS))); // 原野杂草
    public static final DeferredBlock<MoveShapeTallGrassBlock> FALLEN_LAVENDER = registerWithItem("fallen_lavender", () -> new MoveShapeTallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS))); // 沉沦薰衣草
    public static final DeferredBlock<Block> FALLEN_COFFER = registerWithItem("fallen_coffer", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL))); // 沉沦宝匣
    public static final DeferredBlock<MoveShapeTallGrassBlock> TEQUILA = registerWithItem("tequila", () -> new MoveShapeTallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS))); // 龙舌兰
    public static final DeferredBlock<HangingVineHeadBlock> HANGING_VINE_HEAD = registerWithItem("hanging_vine_head", () -> new HangingVineHeadBlock(noRandomTickVineProperties(), RAFBlocks.HANGING_VINE_BODY)); // 垂挂树藤头
    public static final DeferredBlock<HangingVineBodyBlock> HANGING_VINE_BODY = registerWithItem("hanging_vine_body", () -> new HangingVineBodyBlock(noRandomTickVineProperties())); // 垂挂树藤体
    public static final DeferredBlock<Block> PROTOTYPE_BOX = registerHolder("prototype_box"); // 沉沦宝匣

    private static BlockBehaviour.@NotNull Properties noRandomTickVineProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .randomTicks()
                .noCollission()
                .instabreak()
                .sound(SoundType.VINE)
                .pushReaction(PushReaction.DESTROY)
                .offsetType(BlockBehaviour.OffsetType.XZ);
    }

    // endregion 沉沦原野

    // region 桃园谷

    public static final DeferredBlock<StalkBlockHead> KYLIN_BAMBOO_TOP = registerWithItem("kylin_bamboo_top", () -> new StalkBlockHead(noRandomTickBambooProperties(MapColor.COLOR_BLUE), RAFBlocks.KYLIN_BAMBOO_STEM)); // 水玉竹果
    public static final DeferredBlock<StalkBlockStem> KYLIN_BAMBOO_STEM = registerWithItem("kylin_bamboo_stem", () -> new StalkBlockStem(noRandomTickBambooProperties(MapColor.COLOR_BLUE))); // 水玉竹茎
    public static final DeferredBlock<StalkBlockHead> KYLIN_BAMBOO_GREEN_TOP = registerWithItem("kylin_bamboo_green_top", () -> new StalkBlockHead(noRandomTickBambooProperties(MapColor.COLOR_GREEN), RAFBlocks.KYLIN_BAMBOO_GREEN_STEM)); // 翠绿竹果
    public static final DeferredBlock<StalkBlockStem> KYLIN_BAMBOO_GREEN_STEM = registerWithItem("kylin_bamboo_green_stem", () -> new StalkBlockStem(noRandomTickBambooProperties(MapColor.COLOR_GREEN))); // 翠绿竹茎
    public static final DeferredBlock<StalkBlockHead> KYLIN_LOTUS = registerWithItem("kylin_lotus", () -> new StalkBlockHead(BlockBehaviour.Properties.ofFullCopy(Blocks.TORCHFLOWER).mapColor(MapColor.COLOR_LIGHT_BLUE), RAFBlocks.KYLIN_LOTUS_STEM)); // 水玉荷花
    public static final DeferredBlock<KylinLotusStemBlock> KYLIN_LOTUS_STEM = registerWithItem("kylin_lotus_stem", KylinLotusStemBlock::new); // 水玉荷花茎
    public static final DeferredBlock<FloatingKylinLotus> FLOATING_KYLIN_LOTUS = registerWithItem("floating_kylin_lotus", FloatingKylinLotus::new); // 漂浮水玉荷花
    public static final DeferredBlock<FloatingKylinLotus> KYLIN_LOTUS_SMALL = registerWithItem("kylin_lotus_small", KylinLotusSmallBlock::new); // 水玉幽兰花
    public static final DeferredBlock<KylinStalactiteBlock> KYLIN_STALACTITE = registerWithItem("kylin_stalactite", KylinStalactiteBlock::new); // 麒麟钟乳石
    public static final DeferredBlock<Block> KILIN_BOX = registerHolder("kilin_box"); // 麟兽宝匣

    private static BlockBehaviour.Properties noRandomTickBambooProperties(MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .forceSolidOn()
                .instabreak()
                .strength(1.0F)
                .sound(SoundType.BAMBOO)
                .noOcclusion()
                .dynamicShape()
                .offsetType(BlockBehaviour.OffsetType.XZ)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor(Blocks::never);
    }

    // endregion 桃园谷

    // region 占位符

    public static final DeferredBlock<Block> CRYSTAL_SKULL = registerHolder("crystal_skull");
    public static final DeferredBlock<Block> SPAWN_ROBOT = registerHolder("spawn_robot");
    public static final DeferredBlock<Block> SPAWN_ATTACK_ROBOT = registerHolder("spawn_attack_robot");
    public static final DeferredBlock<Block> SPAWN_DEFENSE_TOWER = registerHolder("spawn_defense_tower");
    public static final DeferredBlock<Block> SPAWN_AIRCRAFT = registerHolder("spawn_aircraft");
    public static final DeferredBlock<Block> SPAWN_PRIEST = registerHolder("spawn_priest");
    public static final DeferredBlock<Block> SPAWN_PROTOTYPE = registerHolder("spawn_prototype");
    public static final DeferredBlock<Block> PROTOTYPE_CHEST = registerHolder("prototype_chest");
    public static final DeferredBlock<Block> CONDUCTOR_CHEST = registerHolder("conductor_chest");
    public static final DeferredBlock<Block> KYLIN_CHEST = registerHolder("kylin_chest");
    public static final DeferredBlock<Block> ANGEL_CHEST = registerHolder("angel_chest");
    public static final DeferredBlock<Block> ENERGY_TABLE = registerHolder("energy_table");
    public static final DeferredBlock<Block> MOSSY_COBBLESTONE_BRICKS = registerHolder("mossy_cobblestone_bricks");
    public static final DeferredBlock<Block> MOSSY_COBBLESTONE_TILES = registerHolder("mossy_cobblestone_tiles");
    public static final DeferredBlock<Block> COBBLESTONE_BRICKS = registerHolder("cobblestone_bricks");
    public static final DeferredBlock<Block> COBBLESTONE_TILES = registerHolder("cobblestone_tiles");
    public static final DeferredBlock<Block> STONE_TILES = registerHolder("stone_tiles");
    public static final DeferredBlock<Block> SPAWN_ATTACK_ROBOT_POWER = registerHolder("spawn_attack_robot_power");
    public static final DeferredBlock<Block> SPAWN_PRIEST_POWER = registerHolder("spawn_priest_power");
    public static final DeferredBlock<Block> SPAWN_AIRCRAFT_OLD = registerHolder("spawn_aircraft_old");
    public static final DeferredBlock<Block> KYLIN_POOL_OLD = registerHolder("kylin_pool_old");
    public static final DeferredBlock<Block> GIFT_BOX = registerHolder("gift_box");
    public static final DeferredBlock<Block> SPAWN_DESERT_SOLDIER = registerHolder("spawn_desert_soldier");
    public static final DeferredBlock<Block> SPAWN_TEDDY = registerHolder("spawn_teddy");
    public static final DeferredBlock<Block> RANDOM_PAINTING = registerHolder("random_painting");
    public static final DeferredBlock<Block> SPAWN_JOKER = registerHolder("spawn_joker");
    public static final DeferredBlock<Block> SPAWN_PAINTER = registerHolder("spawn_painter");
    public static final DeferredBlock<Block> SPAWN_JOKER_POWER = registerHolder("spawn_joker_power");
    public static final DeferredBlock<Block> SPAWN_CONDUCTOR = registerHolder("spawn_conductor");
    public static final DeferredBlock<Block> SPAWN_DESERT_BEAST_POWER = registerHolder("spawn_desert_beast_power");
    public static final DeferredBlock<Block> SPAWN_KYLIN_RETINUE = registerHolder("spawn_kylin_retinue");
    public static final DeferredBlock<Block> SPAWN_IMMORTAL_COW_POWER = registerHolder("spawn_immortal_cow_power");

    // endregion

    private static <B extends Block> DeferredBlock<B> registerWithItem(String name, Supplier<B> block, Function<B, BlockItem> item) {
        DeferredBlock<B> holder = BLOCKS.register(name, block);
        RAFItems.BLOCK_ITEMS.register(name, () -> item.apply(holder.get()));
        return holder;
    }

    private static <B extends Block> DeferredBlock<B> registerWithItem(String name, Supplier<B> block) {
        return registerWithItem(name, block, b -> new BlockItem(b, new Item.Properties()));
    }

    private static DeferredBlock<Block> registerHolder(String name) {
        DeferredBlock<Block> block = registerWithItem(name, () -> new Block(BlockBehaviour.Properties.of()));
        addBlockAlias("cataclysm:" + name, block.getId());
        return block;
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
            addBlockAlias("cataclysm:conductor_stone_grass", COVERED_GRASS_STONE.getId());
            addBlockAlias("cataclysm:kylin_stone_grass", COVERED_GRASS_STONE.getId());
            addBlockAlias("cataclysm:boss_thicket", ANCIENT_THICKET.getId());
            addBlockAlias("cataclysm:kylin_lotus_flying", FLOATING_KYLIN_LOTUS.getId());
            addBlockAlias("cataclysm:forest_vine", FOREST_VINE.getId());
        }
    }

    private static void addBlockAlias(String from, ResourceLocation to) {
        BuiltInRegistries.BLOCK.addAlias(ResourceLocation.parse(from), to);
    }

    private static void addBlockEntityAlias(String from, ResourceLocation to) {
        BuiltInRegistries.BLOCK_ENTITY_TYPE.addAlias(ResourceLocation.parse(from), to);
    }
}
