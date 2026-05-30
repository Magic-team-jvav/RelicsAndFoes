package org.magicteam.relicsandfoes.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.world.level.biome.RelicLandBiomeSource;
import org.magicteam.relicsandfoes.world.level.levelgen.RelicLandChunkGenerator;
import org.magicteam.relicsandfoes.world.level.levelgen.feature.TemplateStructureFeature;
import org.magicteam.relicsandfoes.world.level.levelgen.feature.WildfieldCornFeature;
import org.magicteam.relicsandfoes.world.level.levelgen.feature.stateproviders.HorizontalDirectionalStateProvider;
import org.magicteam.relicsandfoes.world.level.levelgen.placement.*;
import org.magicteam.relicsandfoes.world.level.levelgen.structure.*;
import org.magicteam.relicsandfoes.world.level.levelgen.structure.placement.CurrentChunkStructurePlacement;

import java.util.*;
import java.util.function.Consumer;

public final class RAFDimensions {
    public static final ResourceLocation ID = RelicsAndFoes.asResource("relic_land");
    public static final ResourceKey<Level> LEVEL = ResourceKey.create(Registries.DIMENSION, ID);

    private static final DeferredRegister<MapCodec<? extends BiomeSource>> BIOME_SOURCES = DeferredRegister.create(Registries.BIOME_SOURCE, RelicsAndFoes.MODID);
    public static final DeferredHolder<MapCodec<? extends BiomeSource>, MapCodec<RelicLandBiomeSource>> RELIC_LAND_BIOME_SOURCE = BIOME_SOURCES.register(ID.getPath(), () -> RelicLandBiomeSource.CODEC);

    private static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR, RelicsAndFoes.MODID);
    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<RelicLandChunkGenerator>> RELIC_LAND_CHUNK_GENERATOR = CHUNK_GENERATORS.register("relic_land", () -> RelicLandChunkGenerator.CODEC);

    private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, RelicsAndFoes.MODID);
    public static final DeferredHolder<StructureType<?>, StructureType<CrossRealmAncientRuinsStructure>> CROSS_REALM_ANCIENT_RUINS_STRUCTURE = STRUCTURE_TYPES.register("cross_realm_ancient_ruins", () -> () -> CrossRealmAncientRuinsStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<PilgrimageRoadPillarsStructure>> PILGRIMAGE_ROAD_PILLARS_STRUCTURE = STRUCTURE_TYPES.register("pilgrimage_road_pillars", () -> () -> PilgrimageRoadPillarsStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<SkyStoneStructure>> SKY_STONE_STRUCTURE = STRUCTURE_TYPES.register("sky_stone", () -> () -> SkyStoneStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<OnGroundStructure>> ON_GROUND_STRUCTURE = STRUCTURE_TYPES.register("on_ground", () -> () -> OnGroundStructure.CODEC);

    private static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, RelicsAndFoes.MODID);
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<CurrentChunkStructurePlacement>> CURRENT_CHUNK_STRUCTURE_PLACEMENT = STRUCTURE_PLACEMENT_TYPES.register("current_chunk", () -> () -> CurrentChunkStructurePlacement.CODEC);

    private static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, RelicsAndFoes.MODID);
    public static final DeferredHolder<StructurePieceType, StructurePieceType.StructureTemplateType> SIMPLE_TEMPLATE_STRUCTURE_PIECE = STRUCTURE_PIECES.register("simple", () -> SimpleTemplateStructurePiece::new);

    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, RelicsAndFoes.MODID);
    public static final DeferredHolder<Feature<?>, WildfieldCornFeature> WILDFIELD_CORN_FEATURE = FEATURES.register("wildfield_corn", () -> new WildfieldCornFeature(WildfieldCornFeature.Config.CODEC));
    public static final DeferredHolder<Feature<?>, TemplateStructureFeature> TEMPLATE_STRUCTURE_FEATURE = FEATURES.register("template_structure", () -> new TemplateStructureFeature(TemplateStructureFeature.Config.CODEC));

    private static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, RelicsAndFoes.MODID);
    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<NotInCrossRealmAncientRuinsFilter>> NOT_IN_CROSS_REALM_ANCIENT_RUINS_FILTER = PLACEMENT_MODIFIER_TYPES.register("not_in_cross_realm_ancient_ruins", () -> () -> NotInCrossRealmAncientRuinsFilter.CODEC);
    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<NoiseThresholdChancePlacement>> NOISE_THRESHOLD_CHANCE_PLACEMENT = PLACEMENT_MODIFIER_TYPES.register("noise_threshold_chance", () -> () -> NoiseThresholdChancePlacement.CODEC);
    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<InThePeachBlossomValeFilter>> IN_THE_PEACH_BLOSSOM_VALE_FILTER = PLACEMENT_MODIFIER_TYPES.register("in_the_peach_blossom_vale", () -> () -> InThePeachBlossomValeFilter.CODEC);
    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<CheckChanceFilter>> CHECK_CHANCE_FILTER = PLACEMENT_MODIFIER_TYPES.register("check_chance", () -> () -> CheckChanceFilter.CODEC);
    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<WorldSurfaceWithYOffsetPlacement>> WORLD_SURFACE_WITH_Y_OFFSET_PLACEMENT = PLACEMENT_MODIFIER_TYPES.register("world_surface_with_y_offset", () -> () -> WorldSurfaceWithYOffsetPlacement.CODEC);

    private static final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDER_TYPES = DeferredRegister.create(Registries.BLOCK_STATE_PROVIDER_TYPE, RelicsAndFoes.MODID);
    public static final DeferredHolder<BlockStateProviderType<?>, BlockStateProviderType<HorizontalDirectionalStateProvider>> HORIZONTAL_DIRECTIONAL_STATE_PROVIDER = BLOCK_STATE_PROVIDER_TYPES.register("horizontal_directional", () -> new BlockStateProviderType<>(HorizontalDirectionalStateProvider.CODEC));

    public static void register(IEventBus eventBus) {
        BIOME_SOURCES.register(eventBus);
        CHUNK_GENERATORS.register(eventBus);
        STRUCTURE_TYPES.register(eventBus);
        STRUCTURE_PLACEMENT_TYPES.register(eventBus);
        STRUCTURE_PIECES.register(eventBus);
        FEATURES.register(eventBus);
        PLACEMENT_MODIFIER_TYPES.register(eventBus);
        BLOCK_STATE_PROVIDER_TYPES.register(eventBus);
    }

    public static class LevelStemz {
        public static final ResourceKey<LevelStem> KEY = ResourceKey.create(Registries.LEVEL_STEM, ID);

        public static void bootstrap(BootstrapContext<LevelStem> context) {
            HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
            HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
            HolderGetter<NoiseGeneratorSettings> noiseGeneratorSettings = context.lookup(Registries.NOISE_SETTINGS);
            context.register(KEY, new LevelStem(
                    dimensionTypes.getOrThrow(DimensionTypez.KEY),
                    new RelicLandChunkGenerator(
                            RelicLandBiomeSource.create(biomes),
                            noiseGeneratorSettings.getOrThrow(NoiseGeneratorSettingz.KEY)
                    )
            ));
        }
    }

    public static class NoiseGeneratorSettingz {
        public static final ResourceKey<NoiseGeneratorSettings> KEY = ResourceKey.create(Registries.NOISE_SETTINGS, ID);

        public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
            context.register(KEY, new NoiseGeneratorSettings(
                    NoiseSettings.create(0, 256, 1, 1),
                    Blocks.STONE.defaultBlockState(),
                    Blocks.WATER.defaultBlockState(),
                    new NoiseRouter(
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero(),
                            DensityFunctions.zero()
                    ),
                    SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), SurfaceRuleData.BEDROCK),
                    List.of(), 0, false, true, false, false));
        }
    }

    public static class DimensionTypez {
        public static final ResourceKey<DimensionType> KEY = ResourceKey.create(Registries.DIMENSION_TYPE, ID.withSuffix("_type"));

        public static void bootstrap(BootstrapContext<DimensionType> context) {
            context.register(KEY, new DimensionType(
                    OptionalLong.empty(),
                    true,
                    false,
                    false,
                    false,
                    1,
                    false,
                    false,
                    0,
                    256,
                    256,
                    BlockTags.INFINIBURN_OVERWORLD,
                    ID,
                    0.0F,
                    new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)
            ));
        }
    }

    public static class Biomez {
        private static List<ResourceKey<Biome>> allBiomes;

        public static final ResourceKey<Biome> THE_SUNKEN_EXPANSE = key("the_sunken_expanse"); // 沉沦原野
        public static final ResourceKey<Biome> THE_MISTY_SNOWY_PEAKS = key("the_misty_snowy_peaks"); // 迷雾雪峰
        public static final ResourceKey<Biome> THE_RUST_SILENT_CITY = key("the_rust_silent_city"); // 锈寂城
        public static final ResourceKey<Biome> THE_AZURE_SEA = key("the_azure_sea"); // 蔚蓝海
        public static final ResourceKey<Biome> THE_THORNY_DREADLANDS = key("the_thorny_dreadlands"); // 棘悚之地
        public static final ResourceKey<Biome> THE_PEACH_BLOSSOM_VALE = key("the_peach_blossom_vale"); // 桃源谷
        public static final ResourceKey<Biome> THE_SEA_OF_FALLING_STARS = key("the_sea_of_falling_stars"); // 飞星幻海
        public static final ResourceKey<Biome> THE_FOREST_OF_DUSK = key("the_forest_of_dusk"); // 落日之森

        public static List<ResourceKey<Biome>> getAllBiomesForDataGen() {
            if (DatagenModLoader.isRunningDataGen()) {
                return Objects.requireNonNullElseGet(allBiomes, List::of);
            }
            throw new UnsupportedOperationException("Can not get all biomes");
        }

        private static ResourceKey<Biome> key(String path) {
            ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, RelicsAndFoes.asResource(path));
            if (DatagenModLoader.isRunningDataGen()) {
                if (allBiomes == null) {
                    allBiomes = new ArrayList<>();
                }
                allBiomes.add(key);
            }
            return key;
        }

        public static void bootstrap(BootstrapContext<Biome> context) {
            HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
            HolderGetter<ConfiguredWorldCarver<?>> worldCarvers = context.lookup(Registries.CONFIGURED_CARVER);

            context.register(THE_SUNKEN_EXPANSE, new Biome.BiomeBuilder()
                    .temperature(0.8F)
                    .downfall(0.4F)
                    .specialEffects(effects(builder -> builder
                            .waterColor(0x3682A5)
                            .waterFogColor(0x3682A5)
                            .fogColor(0xF3EFBC)
                            .skyColor(0x86C1C4)
                            .grassColorOverride(0x96b030)
                            .foliageColorOverride(0x709D19)))
                    .mobSpawnSettings(mobSpawn(builder -> {}))
                    .generationSettings(generation(placedFeatures, worldCarvers, builder -> {
                        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.BIG_PINE_TREE)
                                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.JUNGLE_TREE)
                                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.X7_PINE_TREE)
                                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.X5_PINE_TREE);
                        addAncientWildgrass(builder);
                        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.WILDFIELD_CORN)
                                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.FALLEN_LAVENDER)
                                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.TEQUILA)
                                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.WILDFIELD_WEEDS);
                        addAbandonedStaff(builder);
                    }))
                    .build());
            context.register(THE_MISTY_SNOWY_PEAKS, OverworldBiomes.jaggedPeaks(placedFeatures, worldCarvers));
            context.register(THE_RUST_SILENT_CITY, OverworldBiomes.savanna(placedFeatures, worldCarvers, false, false));
            context.register(THE_AZURE_SEA, OverworldBiomes.ocean(placedFeatures, worldCarvers, false));
            context.register(THE_THORNY_DREADLANDS, OverworldBiomes.forest(placedFeatures, worldCarvers, true, false, false));
            context.register(THE_PEACH_BLOSSOM_VALE, new Biome.BiomeBuilder()
                    .temperature(0.5F)
                    .downfall(0.8F)
                    .specialEffects(effects(builder -> builder
                            .waterColor(0x5DB7EF)
                            .waterFogColor(0x8DD3AD)
                            .fogColor(0xC8FFE1)
                            .skyColor(0x86C1C4)
                            .grassColorOverride(0xB6D861)
                            .foliageColorOverride(0xB6D861)))
                    .mobSpawnSettings(mobSpawn(builder -> {}))
                    .generationSettings(generation(placedFeatures, worldCarvers, builder -> {
                        builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PlacedFeaturez.BOSS1_HOUSE_ALL);
                        addAncientWildgrass(builder);
                        addAbandonedStaff(builder);
                    }))
                    .build());
            context.register(THE_SEA_OF_FALLING_STARS, OverworldBiomes.lukeWarmOcean(placedFeatures, worldCarvers, false));
            context.register(THE_FOREST_OF_DUSK, OverworldBiomes.forest(placedFeatures, worldCarvers, false, false, false));
        }

        private static void addAncientWildgrass(BiomeGenerationSettings.Builder builder) {
            builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.ANCIENT_WILDGRASS)
                    .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.ANCIENT_TALL_WILDGRASS);
        }

        private static void addAbandonedStaff(BiomeGenerationSettings.Builder builder) {
            builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacedFeaturez.ABANDONED_TABLET)
                    .addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacedFeaturez.ABANDONED_PLANKS)
                    .addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacedFeaturez.ABANDONED_SIGN);
        }

        private static BiomeGenerationSettings generation(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers, Consumer<BiomeGenerationSettings.Builder> consumer) {
            BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
            consumer.accept(builder);
            return builder.build();
        }

        private static BiomeSpecialEffects effects(Consumer<BiomeSpecialEffects.Builder> consumer) {
            BiomeSpecialEffects.Builder builder = new BiomeSpecialEffects.Builder();
            consumer.accept(builder);
            return builder.build();
        }

        private static MobSpawnSettings mobSpawn(Consumer<MobSpawnSettings.Builder> consumer) {
            MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
            consumer.accept(builder);
            return builder.build();
        }
    }

    public static class Structurez {
        public static final ResourceKey<Structure> CROSS_REALM_ANCIENT_RUINS = key("cross_realm_ancient_ruins");
        public static final ResourceKey<Structure> PILGRIMAGE_ROAD_PILLARS = key("pilgrimage_road_pillars");
        public static final ResourceKey<Structure> SKY_STONE = key("sky_stone");
        public static final ResourceKey<Structure> PLAINS_HILL_BIG = key("plains_hill_big");
        public static final ResourceKey<Structure> PLAINS_HILL_SMALL = key("plains_hill_small");
        public static final ResourceKey<Structure> PLAINS_PERISTELE = key("plains_peristele");

        private static ResourceKey<Structure> key(String path) {
            return ResourceKey.create(Registries.STRUCTURE, RelicsAndFoes.asResource(path));
        }

        public static void bootstrap(BootstrapContext<Structure> context) {
            HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
            Holder<Biome> theSunkenExpanse = biomes.getOrThrow(Biomez.THE_SUNKEN_EXPANSE);

            Structure.StructureSettings theSunkenExpanseSettings = new Structure.StructureSettings(HolderSet.direct(theSunkenExpanse));
            context.register(CROSS_REALM_ANCIENT_RUINS, new CrossRealmAncientRuinsStructure(theSunkenExpanseSettings));
            context.register(PILGRIMAGE_ROAD_PILLARS, new PilgrimageRoadPillarsStructure(theSunkenExpanseSettings));
            context.register(SKY_STONE, new SkyStoneStructure(theSunkenExpanseSettings));
            context.register(PLAINS_HILL_BIG, new OnGroundStructure(theSunkenExpanseSettings, "plains_hill_big_", 3, 0.03F, List.of(
                    new BlockPos(24, 0, 24),
                    new BlockPos(24, 0, 24),
                    new BlockPos(24, 0, 24)
            )));
            context.register(PLAINS_HILL_SMALL, new OnGroundStructure(theSunkenExpanseSettings, "plains_hill_small_", 4, 0.25F, List.of(
                    new BlockPos(8, 0, 5),
                    new BlockPos(9, 0, 9),
                    new BlockPos(9, 0, 4),
                    new BlockPos(4, 0, 8)
            )));
            context.register(PLAINS_PERISTELE, new OnGroundStructure(theSunkenExpanseSettings, "plains_peristele_", 5, 0.1F, List.of(
                    new BlockPos(2, 0, 1),
                    new BlockPos(1, 0, 1),
                    new BlockPos(4, 0, 3),
                    new BlockPos(1, 0, 6),
                    new BlockPos(1, 0, 5)
            )));
        }
    }

    public static class StructureSetz {
        public static final ResourceKey<StructureSet> CROSS_REALM_ANCIENT_RUINS = key("cross_realm_ancient_ruins");
        public static final ResourceKey<StructureSet> PILGRIMAGE_ROAD_PILLARS = key("pilgrimage_road_pillars");
        public static final ResourceKey<StructureSet> SKY_STONE = key("sky_stone");
        public static final ResourceKey<StructureSet> PLAINS_HILL_BIG = key("plains_hill_big");
        public static final ResourceKey<StructureSet> PLAINS_HILL_SMALL = key("plains_hill_small");
        public static final ResourceKey<StructureSet> PLAINS_PERISTELE = key("plains_peristele");

        private static ResourceKey<StructureSet> key(String path) {
            return ResourceKey.create(Registries.STRUCTURE_SET, RelicsAndFoes.asResource(path));
        }

        public static void bootstrap(BootstrapContext<StructureSet> context) {
            HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

            CurrentChunkStructurePlacement placement = new CurrentChunkStructurePlacement();
            context.register(CROSS_REALM_ANCIENT_RUINS, new StructureSet(structures.getOrThrow(Structurez.CROSS_REALM_ANCIENT_RUINS), placement));
            context.register(PILGRIMAGE_ROAD_PILLARS, new StructureSet(structures.getOrThrow(Structurez.PILGRIMAGE_ROAD_PILLARS), placement));
            context.register(SKY_STONE, new StructureSet(structures.getOrThrow(Structurez.SKY_STONE), placement));
            context.register(PLAINS_HILL_BIG, new StructureSet(structures.getOrThrow(Structurez.PLAINS_HILL_BIG), placement));
            context.register(PLAINS_HILL_SMALL, new StructureSet(structures.getOrThrow(Structurez.PLAINS_HILL_SMALL), placement));
            context.register(PLAINS_PERISTELE, new StructureSet(structures.getOrThrow(Structurez.PLAINS_PERISTELE), placement));
        }
    }

    public static class ConfiguredFeaturez {
        public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFIELD_CORN = key("wildfield_corn");
        public static final ResourceKey<ConfiguredFeature<?, ?>> ANCIENT_WILDGRASS = key("ancient_wildgrass");
        public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_LAVENDER = key("fallen_lavender");
        public static final ResourceKey<ConfiguredFeature<?, ?>> TEQUILA = key("tequila");
        public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFIELD_WEEDS = key("wildfield_weeds");
        public static final ResourceKey<ConfiguredFeature<?, ?>> ANCIENT_TALL_WILDGRASS = key("ancient_tall_wildgrass");
        public static final ResourceKey<ConfiguredFeature<?, ?>> ABANDONED_TABLET = key("abandoned_tablet");
        public static final ResourceKey<ConfiguredFeature<?, ?>> ABANDONED_PLANKS = key("abandoned_planks");
        public static final ResourceKey<ConfiguredFeature<?, ?>> ABANDONED_SIGN = key("abandoned_sign");
        public static final ResourceKey<ConfiguredFeature<?, ?>> X5_PINE_TREE = key("5x_pine_tree");
        public static final ResourceKey<ConfiguredFeature<?, ?>> X7_PINE_TREE = key("7x_pine_tree");
        public static final ResourceKey<ConfiguredFeature<?, ?>> JUNGLE_TREE = key("jungle_tree");
        public static final ResourceKey<ConfiguredFeature<?, ?>> BIG_PINE_TREE = key("big_pine_tree");
        public static final ResourceKey<ConfiguredFeature<?, ?>> BOSS1_HOUSE_ALL = key("boss1_house_all");
        public static final ResourceKey<ConfiguredFeature<?, ?>> KYLIN_POOL_ALL = key("kylin_pool_all");
        public static final ResourceKey<ConfiguredFeature<?, ?>> BOSS1_TREE_ALL = key("boss1_tree_all");
        public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_PINE_TREE_ALL = key("forest_pine_tree_all");

        private static ResourceKey<ConfiguredFeature<?, ?>> key(String path) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, RelicsAndFoes.asResource(path));
        }

        public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
            register(context, WILDFIELD_CORN, WILDFIELD_CORN_FEATURE.get(), new WildfieldCornFeature.Config(2, 5));
            register(context, ANCIENT_WILDGRASS, Feature.RANDOM_PATCH, VegetationFeatures.grassPatch(BlockStateProvider.simple(RAFBlocks.ANCIENT_WILDGRASS.get()), 24));
            register(context, FALLEN_LAVENDER, Feature.RANDOM_PATCH, VegetationFeatures.grassPatch(BlockStateProvider.simple(RAFBlocks.FALLEN_LAVENDER.get()), 18));
            register(context, TEQUILA, Feature.RANDOM_PATCH, VegetationFeatures.grassPatch(BlockStateProvider.simple(RAFBlocks.TEQUILA.get()), 12));
            register(context, WILDFIELD_WEEDS, Feature.RANDOM_PATCH, VegetationFeatures.grassPatch(BlockStateProvider.simple(RAFBlocks.WILDFIELD_WEEDS.get()), 6));
            register(context, ANCIENT_TALL_WILDGRASS, Feature.RANDOM_PATCH, VegetationFeatures.grassPatch(BlockStateProvider.simple(RAFBlocks.ANCIENT_TALL_WILDGRASS.get()), 6));
            register(context, ABANDONED_TABLET, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new HorizontalDirectionalStateProvider(RAFBlocks.ABANDONED_TABLET.get())));
            register(context, ABANDONED_PLANKS, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new HorizontalDirectionalStateProvider(RAFBlocks.ABANDONED_PLANKS.get())));
            register(context, ABANDONED_SIGN, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new HorizontalDirectionalStateProvider(RAFBlocks.ABANDONED_SIGN.get())));
            register(context, X5_PINE_TREE, Feature.RANDOM_PATCH, new RandomPatchConfiguration(1, 0, 4, PlacementUtils.filtered(
                    TEMPLATE_STRUCTURE_FEATURE.get(),
                    TemplateStructureFeature.Config.of("5x_pine_tree_", 8, 2, 2),
                    isAirAndBelowIsDirt()
            )));
            register(context, X7_PINE_TREE, Feature.RANDOM_PATCH, new RandomPatchConfiguration(1, 0, 4, PlacementUtils.filtered(
                    TEMPLATE_STRUCTURE_FEATURE.get(),
                    TemplateStructureFeature.Config.of("7x_pine_tree_", 3, 3, 3),
                    isAirAndBelowIsDirt()
            )));
            register(context, JUNGLE_TREE, Feature.RANDOM_PATCH, new RandomPatchConfiguration(1, 0, 4, PlacementUtils.filtered(
                    TEMPLATE_STRUCTURE_FEATURE.get(),
                    TemplateStructureFeature.Config.of("jungle_tree_", 3, 9, 9),
                    isAirAndBelowIsDirt()
            )));
            register(context, BIG_PINE_TREE, Feature.RANDOM_PATCH, new RandomPatchConfiguration(1, 0, 4, PlacementUtils.filtered(
                    Feature.SIMPLE_RANDOM_SELECTOR,
                    new SimpleRandomFeatureConfiguration(HolderSet.direct(
                            directPlacedFeature(TEMPLATE_STRUCTURE_FEATURE.get(), TemplateStructureFeature.Config.of("9x_pine_tree_", 3, 4, 4)),
                            directPlacedFeature(TEMPLATE_STRUCTURE_FEATURE.get(), TemplateStructureFeature.Config.of("11x_pine_tree_", 3, 5, 5))
                    )),
                    isAirAndBelowIsDirt()
            )));
            register(context, BOSS1_HOUSE_ALL, TEMPLATE_STRUCTURE_FEATURE.get(), new TemplateStructureFeature.Config(WeightedRandomList.create(
                    TemplateStructureFeature.Template.of("boss1_house_0", 4, 4),
                    TemplateStructureFeature.Template.of("boss1_house_1", 3, 3)
            ), false, true, true));
            register(context, KYLIN_POOL_ALL, TEMPLATE_STRUCTURE_FEATURE.get(), TemplateStructureFeature.Config.of("kylin_pool_", 1, 14, 14));
            register(context, BOSS1_TREE_ALL, Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(
                    directPlacedFeature(TEMPLATE_STRUCTURE_FEATURE.get(), TemplateStructureFeature.Config.of("boss1_tree_", 3, 6, 6)),
                    directPlacedFeature(TEMPLATE_STRUCTURE_FEATURE.get(), TemplateStructureFeature.Config.of("boss1_pink_tree_", 3, 6, 6))
            )));
            register(context, FOREST_PINE_TREE_ALL, TEMPLATE_STRUCTURE_FEATURE.get(), TemplateStructureFeature.Config.of("7x_pine_tree_", 3, 3, 3));
        }

        private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
                BootstrapContext<ConfiguredFeature<?, ?>> context,
                ResourceKey<ConfiguredFeature<?, ?>> key,
                F feature,
                FC config
        ) {
            context.register(key, new ConfiguredFeature<>(feature, config));
        }

        private static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<PlacedFeature> directPlacedFeature(F feature, FC config) {
            return Holder.direct(new PlacedFeature(Holder.direct(new ConfiguredFeature<>(feature, config)), List.of()));
        }

        private static BlockPredicate isAirAndBelowHasSturdyUpFace() {
            return BlockPredicate.allOf(
                    BlockPredicate.ONLY_IN_AIR_PREDICATE,
                    BlockPredicate.hasSturdyFace(Direction.DOWN.getNormal(), Direction.UP)
            );
        }

        private static BlockPredicate isAirAndBelowIsDirt() {
            return BlockPredicate.allOf(
                    BlockPredicate.ONLY_IN_AIR_PREDICATE,
                    BlockPredicate.matchesTag(Direction.DOWN.getNormal(), BlockTags.DIRT)
            );
        }
    }

    public static class PlacedFeaturez {
        public static final ResourceKey<PlacedFeature> WILDFIELD_CORN = key("wildfield_corn");
        public static final ResourceKey<PlacedFeature> ANCIENT_WILDGRASS = key("ancient_wildgrass");
        public static final ResourceKey<PlacedFeature> FALLEN_LAVENDER = key("fallen_lavender");
        public static final ResourceKey<PlacedFeature> TEQUILA = key("tequila");
        public static final ResourceKey<PlacedFeature> WILDFIELD_WEEDS = key("wildfield_weeds");
        public static final ResourceKey<PlacedFeature> ANCIENT_TALL_WILDGRASS = key("ancient_tall_wildgrass");
        public static final ResourceKey<PlacedFeature> ABANDONED_TABLET = key("abandoned_tablet");
        public static final ResourceKey<PlacedFeature> ABANDONED_PLANKS = key("abandoned_planks");
        public static final ResourceKey<PlacedFeature> ABANDONED_SIGN = key("abandoned_sign");
        public static final ResourceKey<PlacedFeature> X5_PINE_TREE = key("5x_pine_tree");
        public static final ResourceKey<PlacedFeature> X7_PINE_TREE = key("7x_pine_tree");
        public static final ResourceKey<PlacedFeature> JUNGLE_TREE = key("jungle_tree");
        public static final ResourceKey<PlacedFeature> BIG_PINE_TREE = key("big_pine_tree");
        public static final ResourceKey<PlacedFeature> BOSS1_HOUSE_ALL = key("boss1_house_all");
        public static final ResourceKey<PlacedFeature> KYLIN_POOL_ALL = key("kylin_pool_all");
        public static final ResourceKey<PlacedFeature> BOSS1_TREE_ALL = key("boss1_tree_all");
        public static final ResourceKey<PlacedFeature> FOREST_PINE_TREE_ALL = key("forest_pine_tree_all");

        private static ResourceKey<PlacedFeature> key(String path) {
            return ResourceKey.create(Registries.PLACED_FEATURE, RelicsAndFoes.asResource(path));
        }

        public static void bootstrap(BootstrapContext<PlacedFeature> context) {
            HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

            register(context, configuredFeatures, WILDFIELD_CORN, ConfiguredFeaturez.WILDFIELD_CORN, CountPlacement.of(UniformInt.of(1, 2)), InSquarePlacement.spread(), NotInCrossRealmAncientRuinsFilter.INSTANCE, BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);
            register(context, configuredFeatures, ANCIENT_WILDGRASS, ConfiguredFeaturez.ANCIENT_WILDGRASS, NoiseThresholdCountPlacement.of(-0.8, 6, 12), InSquarePlacement.spread(), BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);
            register(context, configuredFeatures, FALLEN_LAVENDER, ConfiguredFeaturez.FALLEN_LAVENDER, NoiseThresholdCountPlacement.of(-0.75, 5, 10), InSquarePlacement.spread(), NotInCrossRealmAncientRuinsFilter.INSTANCE, BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);
            register(context, configuredFeatures, TEQUILA, ConfiguredFeaturez.TEQUILA, NoiseThresholdCountPlacement.of(-0.7, 4, 8), InSquarePlacement.spread(), NotInCrossRealmAncientRuinsFilter.INSTANCE, BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);
            register(context, configuredFeatures, WILDFIELD_WEEDS, ConfiguredFeaturez.WILDFIELD_WEEDS, NoiseThresholdCountPlacement.of(-0.65, 3, 6), InSquarePlacement.spread(), BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);
            register(context, configuredFeatures, ANCIENT_TALL_WILDGRASS, ConfiguredFeaturez.ANCIENT_TALL_WILDGRASS, NoiseThresholdCountPlacement.of(-0.6, 3, 6), InSquarePlacement.spread(), BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);
            register(context, configuredFeatures, ABANDONED_TABLET, ConfiguredFeaturez.ABANDONED_TABLET, CountPlacement.of(UniformInt.of(1, 2)), InSquarePlacement.spread(), NotInCrossRealmAncientRuinsFilter.INSTANCE, BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BlockPredicateFilter.forPredicate(ConfiguredFeaturez.isAirAndBelowHasSturdyUpFace()));
            register(context, configuredFeatures, ABANDONED_PLANKS, ConfiguredFeaturez.ABANDONED_PLANKS, InSquarePlacement.spread(), BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BlockPredicateFilter.forPredicate(ConfiguredFeaturez.isAirAndBelowHasSturdyUpFace()));
            register(context, configuredFeatures, ABANDONED_SIGN, ConfiguredFeaturez.ABANDONED_SIGN, InSquarePlacement.spread(), NotInCrossRealmAncientRuinsFilter.INSTANCE, BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BlockPredicateFilter.forPredicate(ConfiguredFeaturez.isAirAndBelowHasSturdyUpFace()));
            register(context, configuredFeatures, X5_PINE_TREE, ConfiguredFeaturez.X5_PINE_TREE, CountPlacement.of(9), InSquarePlacement.spread(), NotInCrossRealmAncientRuinsFilter.INSTANCE, BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);
            register(context, configuredFeatures, X7_PINE_TREE, ConfiguredFeaturez.X7_PINE_TREE, CountPlacement.of(2), InSquarePlacement.spread(), NotInCrossRealmAncientRuinsFilter.INSTANCE, BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);
            register(context, configuredFeatures, JUNGLE_TREE, ConfiguredFeaturez.JUNGLE_TREE, InSquarePlacement.spread(), NotInCrossRealmAncientRuinsFilter.INSTANCE, BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, new NoiseThresholdChancePlacement(0, 0.5F, 0.25F));
            register(context, configuredFeatures, BIG_PINE_TREE, ConfiguredFeaturez.BIG_PINE_TREE, NoiseThresholdCountPlacement.of(-0.25, 10, 0), InSquarePlacement.spread(), NotInCrossRealmAncientRuinsFilter.INSTANCE, BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);
            register(context, configuredFeatures, BOSS1_HOUSE_ALL, ConfiguredFeaturez.BOSS1_HOUSE_ALL, InSquarePlacement.spread(), InThePeachBlossomValeFilter.of(TriState.FALSE, TriState.TRUE), BiomeFilter.biome(), WorldSurfaceWithYOffsetPlacement.of(-3), BlockPredicateFilter.forPredicate(BlockPredicate.noFluid(new Vec3i(3, 2, 3))), CheckChanceFilter.of(0.075F));
        }

        private static void register(
                BootstrapContext<PlacedFeature> context,
                HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures,
                ResourceKey<PlacedFeature> placed,
                ResourceKey<ConfiguredFeature<?, ?>> configured,
                PlacementModifier... modifiers
        ) {
            context.register(placed, new PlacedFeature(configuredFeatures.getOrThrow(configured), Arrays.asList(modifiers)));
        }
    }
}
