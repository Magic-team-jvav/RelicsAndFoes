package org.magicteam.relicsandfoes.init;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.worldgen.FlipLessThanY;
import org.magicteam.relicsandfoes.worldgen.RelicLandBiomeSource;
import org.magicteam.relicsandfoes.worldgen.RelicLandChunkGenerator;

import java.util.List;
import java.util.OptionalLong;

public final class ModDimensions {
    public static final ResourceLocation ID = RelicsAndFoes.asResource("relic_land");
    public static final ResourceKey<Level> LEVEL = ResourceKey.create(Registries.DIMENSION, ID);
    public static final int SEA_LEVEL = 31;

    private static final DeferredRegister<MapCodec<? extends BiomeSource>> BIOME_SOURCES = DeferredRegister.create(Registries.BIOME_SOURCE, RelicsAndFoes.MODID);
    public static final DeferredHolder<MapCodec<? extends BiomeSource>, MapCodec<RelicLandBiomeSource>> RELIC_LAND_BIOME_SOURCE = BIOME_SOURCES.register(ID.getPath(), () -> RelicLandBiomeSource.CODEC);

    private static final DeferredRegister<MapCodec<? extends DensityFunction>> DENSITY_FUNCTION_TYPES = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, RelicsAndFoes.MODID);
    public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<FlipLessThanY>> FLIP_LESS_THAN_Y = DENSITY_FUNCTION_TYPES.register("flip_less_than_y", () -> FlipLessThanY.DATA_CODEC);

    public static void register(IEventBus eventBus) {
        BIOME_SOURCES.register(eventBus);
        DENSITY_FUNCTION_TYPES.register(eventBus);
    }

    public static NoiseChunk.BlockStateFiller filler(NormalNoise noise) {
        return context -> {
            int worldx = context.blockX();
            int worldy = context.blockY();
            int worldz = context.blockZ();
            int middle_x = (worldx + 1024) / 2048 * 2048;
            int middle_z = (worldz + 1024) / 2048 * 2048;
            double jvli = Math.sqrt(Mth.square(middle_x - worldx) + Mth.square(middle_z - worldz));
            double height_bili = Mth.clamp((220 - jvli) / 50, 0, 1);
            double height_plain = 40 + (0.5 + noise.getValue(worldx, 0, worldz)) * 10;
            int height = (int) (height_plain * (1 - height_bili) + height_bili * 52);
            if (worldy > height) {
                return null;
            } else if (worldy > height - 1) {
                return Blocks.GRASS_BLOCK.defaultBlockState();
            } else if (worldy > height - 5) {
                return Blocks.DIRT.defaultBlockState();
            }
            return Blocks.STONE.defaultBlockState();
        };
    }

    public static class LevelStems {
        public static final ResourceKey<LevelStem> KEY = ResourceKey.create(Registries.LEVEL_STEM, ID);

        public static void bootstrap(BootstrapContext<LevelStem> context) {
            HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
            HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
            HolderGetter<NoiseGeneratorSettings> noiseGeneratorSettings = context.lookup(Registries.NOISE_SETTINGS);
            Climate.Parameter fullRange = Climate.Parameter.span(-1, 1);
            context.register(KEY, new LevelStem(
                    dimensionTypes.getOrThrow(DimensionTypez.KEY),
                    new RelicLandChunkGenerator(
                            MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(List.of(
                                    new Pair<>(Climate.parameters(
                                            Climate.Parameter.span(-0.45F, -0.15F),
                                            Climate.Parameter.span(-1, -0.1F),
                                            Climate.Parameter.span(-0.19F, 1.0F),
                                            Climate.Parameter.span(0.55F, 1.0F),
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(-1, 1),
                                            0
                                    ), biomes.getOrThrow(ModDimensions.Biomez.THE_SUNKEN_EXPANSE)),
                                    new Pair<>(Climate.parameters(
                                            Climate.Parameter.span(-1.0F, -0.45F),
                                            Climate.Parameter.span(-1.0F, -0.35F),
                                            Climate.Parameter.span(-0.19F, 1.0F),
                                            Climate.Parameter.span(-1.0F, -0.78F),
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(-1, 1),
                                            0
                                    ), biomes.getOrThrow(ModDimensions.Biomez.THE_MISTY_SNOWY_PEAKS)),
                                    new Pair<>(Climate.parameters(
                                            Climate.Parameter.span(0.2F, 0.55F),
                                            Climate.Parameter.span(-0.35F, -0.1F),
                                            Climate.Parameter.span(-0.19F, 1.0F),
                                            Climate.Parameter.span(0.45F, 0.55F),
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(-1, 1),
                                            0
                                    ), biomes.getOrThrow(ModDimensions.Biomez.THE_RUST_SILENT_CITY)),
                                    new Pair<>(Climate.parameters(
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(-1.2F, -0.19F),
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(-1, 0),
                                            0
                                    ), biomes.getOrThrow(ModDimensions.Biomez.THE_AZURE_SEA)),
                                    new Pair<>(Climate.parameters(
                                            Climate.Parameter.span(-0.45F, 0.2F),
                                            Climate.Parameter.span(-0.35F, 0.1F),
                                            Climate.Parameter.span(-0.19F, 1.0F),
                                            Climate.Parameter.span(-1, -0.375F),
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(-1, -0.5F),
                                            0
                                    ), biomes.getOrThrow(ModDimensions.Biomez.THE_THORNY_DREADLANDS)),
                                    new Pair<>(Climate.parameters(
                                            Climate.Parameter.span(-0.45F, 0.2F),
                                            Climate.Parameter.span(-1, -0.1F),
                                            Climate.Parameter.span(0.03F, 1.0F),
                                            Climate.Parameter.span(-0.78F, -0.375F),
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(0.5F, 1),
                                            0
                                    ), biomes.getOrThrow(ModDimensions.Biomez.THE_PEACH_BLOSSOM_VALE)),
                                    new Pair<>(Climate.parameters(
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(-1.2F, -0.19F),
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(0, 1),
                                            0
                                    ), biomes.getOrThrow(ModDimensions.Biomez.THE_SEA_OF_FALLING_STARS)),
                                    new Pair<>(Climate.parameters(
                                            Climate.Parameter.span(-0.45F, 0.2F),
                                            Climate.Parameter.span(-1, -0.1F),
                                            Climate.Parameter.span(0.03F, 1.0F),
                                            Climate.Parameter.span(-0.78F, -0.375F),
                                            Climate.Parameter.span(-1, 1),
                                            Climate.Parameter.span(-0.5F, 0.5F),
                                            0
                                    ), biomes.getOrThrow(ModDimensions.Biomez.THE_FOREST_OF_DUSK))
                            ))),
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

        private static DensityFunction slideRelicLand(DensityFunction densityFunction) {
            return NoiseRouterData.slide(densityFunction, 0, 256, 16, 0, -0.078125, 0, 24, 0.4);
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
                    true,
                    1,
                    true,
                    true,
                    0,
                    256,
                    256,
                    BlockTags.INFINIBURN_OVERWORLD,
                    BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                    0.0F,
                    new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0)
            ));
        }
    }

    public static class Biomez {
        public static final ResourceKey<Biome> THE_SUNKEN_EXPANSE = ResourceKey.create(Registries.BIOME, RelicsAndFoes.asResource("the_sunken_expanse")); // 沉沦原野
        public static final ResourceKey<Biome> THE_MISTY_SNOWY_PEAKS = ResourceKey.create(Registries.BIOME, RelicsAndFoes.asResource("the_misty_snowy_peaks")); // 迷雾雪峰
        public static final ResourceKey<Biome> THE_RUST_SILENT_CITY = ResourceKey.create(Registries.BIOME, RelicsAndFoes.asResource("the_rust_silent_city")); // 锈寂城
        public static final ResourceKey<Biome> THE_AZURE_SEA = ResourceKey.create(Registries.BIOME, RelicsAndFoes.asResource("the_azure_sea")); // 蔚蓝海
        public static final ResourceKey<Biome> THE_THORNY_DREADLANDS = ResourceKey.create(Registries.BIOME, RelicsAndFoes.asResource("the_thorny_dreadlands")); // 棘悚之地
        public static final ResourceKey<Biome> THE_PEACH_BLOSSOM_VALE = ResourceKey.create(Registries.BIOME, RelicsAndFoes.asResource("the_peach_blossom_vale")); // 桃源谷
        public static final ResourceKey<Biome> THE_SEA_OF_FALLING_STARS = ResourceKey.create(Registries.BIOME, RelicsAndFoes.asResource("the_sea_of_falling_stars")); // 飞星幻海
        public static final ResourceKey<Biome> THE_FOREST_OF_DUSK = ResourceKey.create(Registries.BIOME, RelicsAndFoes.asResource("the_forest_of_dusk")); // 落日之森

        public static void bootstrap(BootstrapContext<Biome> context) {
            HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
            HolderGetter<ConfiguredWorldCarver<?>> worldCarvers = context.lookup(Registries.CONFIGURED_CARVER);

            context.register(THE_SUNKEN_EXPANSE, OverworldBiomes.plains(placedFeatures, worldCarvers, false, false, false));
            context.register(THE_MISTY_SNOWY_PEAKS, OverworldBiomes.jaggedPeaks(placedFeatures, worldCarvers));
            context.register(THE_RUST_SILENT_CITY, OverworldBiomes.savanna(placedFeatures, worldCarvers, false, false));
            context.register(THE_AZURE_SEA, OverworldBiomes.ocean(placedFeatures, worldCarvers, false));
            context.register(THE_THORNY_DREADLANDS, OverworldBiomes.forest(placedFeatures, worldCarvers, true, false, false));
            context.register(THE_PEACH_BLOSSOM_VALE, OverworldBiomes.meadowOrCherryGrove(placedFeatures, worldCarvers, true));
            context.register(THE_SEA_OF_FALLING_STARS, OverworldBiomes.lukeWarmOcean(placedFeatures, worldCarvers, false));
            context.register(THE_FOREST_OF_DUSK, OverworldBiomes.forest(placedFeatures, worldCarvers, false, false, false));
        }
    }

    public static class Noisez {
        public static final ResourceKey<NormalNoise.NoiseParameters> EROSION = ResourceKey.create(Registries.NOISE, RelicsAndFoes.asResource("erosion"));
        public static final ResourceKey<NormalNoise.NoiseParameters> RIDGE = ResourceKey.create(Registries.NOISE, RelicsAndFoes.asResource("ridge"));

        public static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> context) {
            context.register(EROSION, new NormalNoise.NoiseParameters(-11, 0.1, 0.1, 0, 0.1, 0.1));
            context.register(RIDGE, new NormalNoise.NoiseParameters(-9, 0.2, 0.4, 0.2, 0, 0, 0));
        }
    }
}
