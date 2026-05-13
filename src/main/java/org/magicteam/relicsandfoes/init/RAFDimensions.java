package org.magicteam.relicsandfoes.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
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
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.levelgen.RelicLandBiomeSource;
import org.magicteam.relicsandfoes.levelgen.RelicLandChunkGenerator;
import org.magicteam.relicsandfoes.levelgen.structure.RuinCityStructure;
import org.magicteam.relicsandfoes.levelgen.structure.SuperTemplateStructurePiece;
import org.magicteam.relicsandfoes.levelgen.structure.placement.SimpleStructurePlacement;

import java.util.List;
import java.util.OptionalLong;

public final class RAFDimensions {
    public static final ResourceLocation ID = RelicsAndFoes.asResource("relic_land");
    public static final ResourceKey<Level> LEVEL = ResourceKey.create(Registries.DIMENSION, ID);
    public static final int SEA_LEVEL = 0;

    private static final DeferredRegister<MapCodec<? extends BiomeSource>> BIOME_SOURCES = DeferredRegister.create(Registries.BIOME_SOURCE, RelicsAndFoes.MODID);
    public static final DeferredHolder<MapCodec<? extends BiomeSource>, MapCodec<RelicLandBiomeSource>> RELIC_LAND_BIOME_SOURCE = BIOME_SOURCES.register(ID.getPath(), () -> RelicLandBiomeSource.CODEC);

    private static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR, RelicsAndFoes.MODID);
    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<RelicLandChunkGenerator>> RELIC_LAND_CHUNK_GENERATOR = CHUNK_GENERATORS.register("relic_land", () -> RelicLandChunkGenerator.CODEC);

    private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, RelicsAndFoes.MODID);
    public static final DeferredHolder<StructureType<?>, StructureType<RuinCityStructure>> RUIN_CITY_STRUCTURE = STRUCTURE_TYPES.register("ruin_city", () -> () -> RuinCityStructure.CODEC);

    private static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, RelicsAndFoes.MODID);
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<SimpleStructurePlacement>> SIMPLE_STRUCTURE_PLACEMENT = STRUCTURE_PLACEMENT_TYPES.register("simple", () -> () -> SimpleStructurePlacement.CODEC);

    private static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, RelicsAndFoes.MODID);
    public static final DeferredHolder<StructurePieceType, StructurePieceType.StructureTemplateType> SUPER_TEMPLATE_STRUCTURE_PIECE = STRUCTURE_PIECES.register("super_template", () -> SuperTemplateStructurePiece::new);

    public static void register(IEventBus eventBus) {
        BIOME_SOURCES.register(eventBus);
        CHUNK_GENERATORS.register(eventBus);
        STRUCTURE_TYPES.register(eventBus);
        STRUCTURE_PLACEMENT_TYPES.register(eventBus);
        STRUCTURE_PIECES.register(eventBus);
    }

    public static class LevelStems {
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
                    SurfaceRuleData.overworld(),
                    List.of(), SEA_LEVEL, false, true, false, false));
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
        public static final ResourceKey<Biome> THE_SUNKEN_EXPANSE = key("the_sunken_expanse"); // 沉沦原野
        public static final ResourceKey<Biome> THE_MISTY_SNOWY_PEAKS = key("the_misty_snowy_peaks"); // 迷雾雪峰
        public static final ResourceKey<Biome> THE_RUST_SILENT_CITY = key("the_rust_silent_city"); // 锈寂城
        public static final ResourceKey<Biome> THE_AZURE_SEA = key("the_azure_sea"); // 蔚蓝海
        public static final ResourceKey<Biome> THE_THORNY_DREADLANDS = key("the_thorny_dreadlands"); // 棘悚之地
        public static final ResourceKey<Biome> THE_PEACH_BLOSSOM_VALE = key("the_peach_blossom_vale"); // 桃源谷
        public static final ResourceKey<Biome> THE_SEA_OF_FALLING_STARS = key("the_sea_of_falling_stars"); // 飞星幻海
        public static final ResourceKey<Biome> THE_FOREST_OF_DUSK = key("the_forest_of_dusk"); // 落日之森

        private static ResourceKey<Biome> key(String path) {
            return ResourceKey.create(Registries.BIOME, RelicsAndFoes.asResource(path));
        }

        public static void bootstrap(BootstrapContext<Biome> context) {
            HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
            HolderGetter<ConfiguredWorldCarver<?>> worldCarvers = context.lookup(Registries.CONFIGURED_CARVER);

            context.register(THE_SUNKEN_EXPANSE, new Biome.BiomeBuilder()
                    .hasPrecipitation(false)
                    .temperature(0.8F)
                    .downfall(0.4F)
                    .specialEffects(new BiomeSpecialEffects.Builder()
                            .waterColor(0x3682A5)
                            .waterFogColor(0x3682A5)
                            .fogColor(0xF3EFBC)
                            .skyColor(0x86C1C4)
                            .backgroundMusic(null)
                            .build())
                    .mobSpawnSettings(new MobSpawnSettings.Builder()
                            .build())
                    .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers)
                            .build())
                    .build());
            context.register(THE_MISTY_SNOWY_PEAKS, OverworldBiomes.jaggedPeaks(placedFeatures, worldCarvers));
            context.register(THE_RUST_SILENT_CITY, OverworldBiomes.savanna(placedFeatures, worldCarvers, false, false));
            context.register(THE_AZURE_SEA, OverworldBiomes.ocean(placedFeatures, worldCarvers, false));
            context.register(THE_THORNY_DREADLANDS, OverworldBiomes.forest(placedFeatures, worldCarvers, true, false, false));
            context.register(THE_PEACH_BLOSSOM_VALE, OverworldBiomes.meadowOrCherryGrove(placedFeatures, worldCarvers, true));
            context.register(THE_SEA_OF_FALLING_STARS, OverworldBiomes.lukeWarmOcean(placedFeatures, worldCarvers, false));
            context.register(THE_FOREST_OF_DUSK, OverworldBiomes.forest(placedFeatures, worldCarvers, false, false, false));
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
}
