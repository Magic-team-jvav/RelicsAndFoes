package org.magicteam.relicsandfoes.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.world.level.biome.RelicLandBiomeSource;
import org.magicteam.relicsandfoes.world.level.levelgen.RelicLandChunkGenerator;
import org.magicteam.relicsandfoes.world.level.levelgen.feature.WildfieldCornFeature;
import org.magicteam.relicsandfoes.world.level.levelgen.feature.stateproviders.HorizontalDirectionalStateProvider;
import org.magicteam.relicsandfoes.world.level.levelgen.placement.ExcludeRegionsPlacement;
import org.magicteam.relicsandfoes.world.level.levelgen.structure.RuinCityStructure;
import org.magicteam.relicsandfoes.world.level.levelgen.structure.SimpleTemplateStructurePiece;
import org.magicteam.relicsandfoes.world.level.levelgen.structure.placement.SimpleStructurePlacement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.function.Consumer;

public final class RAFDimensions {
    public static final ResourceLocation ID = RelicsAndFoes.asResource("relic_land");
    public static final ResourceKey<Level> LEVEL = ResourceKey.create(Registries.DIMENSION, ID);

    private static final DeferredRegister<MapCodec<? extends BiomeSource>> BIOME_SOURCES = DeferredRegister.create(Registries.BIOME_SOURCE, RelicsAndFoes.MODID);
    public static final DeferredHolder<MapCodec<? extends BiomeSource>, MapCodec<RelicLandBiomeSource>> RELIC_LAND_BIOME_SOURCE = BIOME_SOURCES.register(ID.getPath(), () -> RelicLandBiomeSource.CODEC);

    private static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR, RelicsAndFoes.MODID);
    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<RelicLandChunkGenerator>> RELIC_LAND_CHUNK_GENERATOR = CHUNK_GENERATORS.register("relic_land", () -> RelicLandChunkGenerator.CODEC);

    private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, RelicsAndFoes.MODID);
    public static final DeferredHolder<StructureType<?>, StructureType<RuinCityStructure>> RUIN_CITY_STRUCTURE = STRUCTURE_TYPES.register("ruin_city", () -> () -> RuinCityStructure.CODEC);

    private static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, RelicsAndFoes.MODID);
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<SimpleStructurePlacement>> SIMPLE_STRUCTURE_PLACEMENT = STRUCTURE_PLACEMENT_TYPES.register("simple", () -> () -> SimpleStructurePlacement.CODEC);

    private static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, RelicsAndFoes.MODID);
    public static final DeferredHolder<StructurePieceType, StructurePieceType.StructureTemplateType> SIMPLE_TEMPLATE_STRUCTURE_PIECE = STRUCTURE_PIECES.register("simple", () -> SimpleTemplateStructurePiece::new);

    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, RelicsAndFoes.MODID);
    public static final DeferredHolder<Feature<?>, WildfieldCornFeature> WILDFIELD_CORN_FEATURE = FEATURES.register("wildfield_corn", () -> new WildfieldCornFeature(WildfieldCornFeature.Config.CODEC));

    private static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, RelicsAndFoes.MODID);
    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<ExcludeRegionsPlacement>> EXCLUDE_REGIONS_PLACEMENT = PLACEMENT_MODIFIER_TYPES.register("exclude_regions", () -> () -> ExcludeRegionsPlacement.CODEC);

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
                    .hasPrecipitation(true)
                    .temperature(0.8F)
                    .downfall(0.4F)
                    .specialEffects(new BiomeSpecialEffects.Builder()
                            .waterColor(0x3682A5)
                            .waterFogColor(0x3682A5)
                            .fogColor(0xF3EFBC)
                            .skyColor(0x86C1C4)
                            .grassColorOverride(0x96b030)
                            .foliageColorOverride(0x709D19)
                            .backgroundMusic(null) // 代码控制
                            .build())
                    .mobSpawnSettings(new MobSpawnSettings.Builder()
                            .build())
                    .generationSettings(generation(placedFeatures, worldCarvers, builder -> builder
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.WILDFIELD_CORN)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.ANCIENT_WILDGRASS)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.FALLEN_LAVENDER)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.TEQUILA)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.WILDFIELD_WEEDS)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturez.ANCIENT_TALL_WILDGRASS)
                            .addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacedFeaturez.ABANDONED_TABLET)
                            .addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacedFeaturez.ABANDONED_PLANKS)
                            .addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacedFeaturez.ABANDONED_SIGN)
                    ))
                    .build());
            context.register(THE_MISTY_SNOWY_PEAKS, OverworldBiomes.jaggedPeaks(placedFeatures, worldCarvers));
            context.register(THE_RUST_SILENT_CITY, OverworldBiomes.savanna(placedFeatures, worldCarvers, false, false));
            context.register(THE_AZURE_SEA, OverworldBiomes.ocean(placedFeatures, worldCarvers, false));
            context.register(THE_THORNY_DREADLANDS, OverworldBiomes.forest(placedFeatures, worldCarvers, true, false, false));
            context.register(THE_PEACH_BLOSSOM_VALE, OverworldBiomes.meadowOrCherryGrove(placedFeatures, worldCarvers, true));
            context.register(THE_SEA_OF_FALLING_STARS, OverworldBiomes.lukeWarmOcean(placedFeatures, worldCarvers, false));
            context.register(THE_FOREST_OF_DUSK, OverworldBiomes.forest(placedFeatures, worldCarvers, false, false, false));
        }

        private static BiomeGenerationSettings generation(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers, Consumer<BiomeGenerationSettings.Builder> consumer) {
            BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
            consumer.accept(builder);
            return builder.build();
        }
    }

    public static class Structurez {
        public static final ResourceKey<Structure> RUIN_CITY = key("ruin_city");

        private static ResourceKey<Structure> key(String path) {
            return ResourceKey.create(Registries.STRUCTURE, RelicsAndFoes.asResource(path));
        }

        public static void bootstrap(BootstrapContext<Structure> context) {
            HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
            Holder<Biome> theSunkenExpanse = biomes.getOrThrow(Biomez.THE_SUNKEN_EXPANSE);

            context.register(RUIN_CITY, new RuinCityStructure(new Structure.StructureSettings(HolderSet.direct(theSunkenExpanse))));
        }
    }

    public static class StructureSetz {
        public static final ResourceKey<StructureSet> RUIN_CITY = key("ruin_city");

        private static ResourceKey<StructureSet> key(String path) {
            return ResourceKey.create(Registries.STRUCTURE_SET, RelicsAndFoes.asResource(path));
        }

        public static void bootstrap(BootstrapContext<StructureSet> context) {
            HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

            context.register(RUIN_CITY, new StructureSet(structures.getOrThrow(Structurez.RUIN_CITY), SimpleStructurePlacement.INSTANCE));
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

        private static ResourceKey<ConfiguredFeature<?, ?>> key(String path) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, RelicsAndFoes.asResource(path));
        }

        public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
            context.register(WILDFIELD_CORN, new ConfiguredFeature<>(WILDFIELD_CORN_FEATURE.get(), new WildfieldCornFeature.Config(2, 5)));
            context.register(ANCIENT_WILDGRASS, new ConfiguredFeature<>(Feature.RANDOM_PATCH, VegetationFeatures.grassPatch(BlockStateProvider.simple(RAFBlocks.ANCIENT_WILDGRASS.get()), 24)));
            context.register(FALLEN_LAVENDER, new ConfiguredFeature<>(Feature.RANDOM_PATCH, VegetationFeatures.grassPatch(BlockStateProvider.simple(RAFBlocks.FALLEN_LAVENDER.get()), 18)));
            context.register(TEQUILA, new ConfiguredFeature<>(Feature.RANDOM_PATCH, VegetationFeatures.grassPatch(BlockStateProvider.simple(RAFBlocks.TEQUILA.get()), 12)));
            context.register(WILDFIELD_WEEDS, new ConfiguredFeature<>(Feature.RANDOM_PATCH, VegetationFeatures.grassPatch(BlockStateProvider.simple(RAFBlocks.WILDFIELD_WEEDS.get()), 6)));
            context.register(ANCIENT_TALL_WILDGRASS, new ConfiguredFeature<>(Feature.RANDOM_PATCH, VegetationFeatures.grassPatch(BlockStateProvider.simple(RAFBlocks.ANCIENT_TALL_WILDGRASS.get()), 6)));
            context.register(ABANDONED_TABLET, new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(1, 7, 0, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new HorizontalDirectionalStateProvider(RAFBlocks.ABANDONED_TABLET.get())), isAirAndBelowHasSturdyUPFace()))));
            context.register(ABANDONED_PLANKS, new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(1, 7, 0, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new HorizontalDirectionalStateProvider(RAFBlocks.ABANDONED_PLANKS.get())), isAirAndBelowHasSturdyUPFace()))));
            context.register(ABANDONED_SIGN, new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(1, 7, 0, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new HorizontalDirectionalStateProvider(RAFBlocks.ABANDONED_SIGN.get())), isAirAndBelowHasSturdyUPFace()))));
        }

        private static BlockPredicate isAirAndBelowHasSturdyUPFace() {
            return BlockPredicate.allOf(
                    BlockPredicate.ONLY_IN_AIR_PREDICATE,
                    BlockPredicate.hasSturdyFace(Direction.DOWN.getNormal(), Direction.UP)
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

        private static ResourceKey<PlacedFeature> key(String path) {
            return ResourceKey.create(Registries.PLACED_FEATURE, RelicsAndFoes.asResource(path));
        }

        public static void bootstrap(BootstrapContext<PlacedFeature> context) {
            HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

            context.register(WILDFIELD_CORN, new PlacedFeature(configuredFeatures.getOrThrow(ConfiguredFeaturez.WILDFIELD_CORN), List.of(BiomeFilter.biome(), ExcludeRegionsPlacement.INSTANCE, CountPlacement.of(UniformInt.of(1, 2)), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
            context.register(ANCIENT_WILDGRASS, new PlacedFeature(configuredFeatures.getOrThrow(ConfiguredFeaturez.ANCIENT_WILDGRASS), List.of(BiomeFilter.biome(), NoiseThresholdCountPlacement.of(-0.8, 6, 12), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
            context.register(FALLEN_LAVENDER, new PlacedFeature(configuredFeatures.getOrThrow(ConfiguredFeaturez.FALLEN_LAVENDER), List.of(BiomeFilter.biome(), ExcludeRegionsPlacement.INSTANCE, NoiseThresholdCountPlacement.of(-0.75, 5, 10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
            context.register(TEQUILA, new PlacedFeature(configuredFeatures.getOrThrow(ConfiguredFeaturez.TEQUILA), List.of(BiomeFilter.biome(), ExcludeRegionsPlacement.INSTANCE, NoiseThresholdCountPlacement.of(-0.7, 4, 8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
            context.register(WILDFIELD_WEEDS, new PlacedFeature(configuredFeatures.getOrThrow(ConfiguredFeaturez.WILDFIELD_WEEDS), List.of(BiomeFilter.biome(), NoiseThresholdCountPlacement.of(-0.65, 3, 6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
            context.register(ANCIENT_TALL_WILDGRASS, new PlacedFeature(configuredFeatures.getOrThrow(ConfiguredFeaturez.ANCIENT_TALL_WILDGRASS), List.of(BiomeFilter.biome(), NoiseThresholdCountPlacement.of(-0.6, 3, 6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
            context.register(ABANDONED_TABLET, new PlacedFeature(configuredFeatures.getOrThrow(ConfiguredFeaturez.ABANDONED_TABLET), List.of(BiomeFilter.biome(), ExcludeRegionsPlacement.INSTANCE, CountPlacement.of(UniformInt.of(1, 2)), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
            context.register(ABANDONED_PLANKS, new PlacedFeature(configuredFeatures.getOrThrow(ConfiguredFeaturez.ABANDONED_PLANKS), List.of(BiomeFilter.biome(), ExcludeRegionsPlacement.INSTANCE, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
            context.register(ABANDONED_SIGN, new PlacedFeature(configuredFeatures.getOrThrow(ConfiguredFeaturez.ABANDONED_SIGN), List.of(BiomeFilter.biome(), ExcludeRegionsPlacement.INSTANCE, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
        }
    }
}
