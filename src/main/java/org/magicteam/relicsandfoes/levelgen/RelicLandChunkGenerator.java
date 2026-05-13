package org.magicteam.relicsandfoes.levelgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class RelicLandChunkGenerator extends NoiseBasedChunkGenerator {
    public static final MapCodec<RelicLandChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(RelicLandChunkGenerator::getBiomeSource),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(RelicLandChunkGenerator::generatorSettings)
    ).apply(instance, instance.stable(RelicLandChunkGenerator::new)));
    protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
    protected static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.defaultBlockState();
    protected static final BlockState DIRT = Blocks.DIRT.defaultBlockState();
    protected static final BlockState STONE = Blocks.STONE.defaultBlockState();

    protected NormalNoise noise;

    public RelicLandChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource, settings);
    }

    @Override
    protected MapCodec<RelicLandChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    protected NoiseChunk createNoiseChunk(ChunkAccess chunk, StructureManager structureManager, Blender blender, RandomState random) {
        NoiseChunk noiseChunk = super.createNoiseChunk(chunk, structureManager, blender, random);
        setupFiller(noiseChunk);
        return noiseChunk;
    }

    public void setupFiller(NoiseChunk noiseChunk) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        if (noise == null) {
            RandomSource randomSource = RandomSource.create(server.getWorldData().worldGenOptions().seed());
            double[] amplitudes = new double[randomSource.nextInt(3, 6)];
            for (int j = 0; j < amplitudes.length; j++) {
                amplitudes[j] = randomSource.nextDouble();
            }
            this.noise = NormalNoise.create(randomSource, -9, amplitudes);
        }
        noiseChunk.blockStateRule = context -> {
            int worldX = context.blockX();
            int worldY = context.blockY();
            int worldZ = context.blockZ();
            int middleX = (worldX + 1024) >> 11 << 11;
            int middleZ = (worldZ + 1024) >> 11 << 11;
            double offset = Math.sqrt(Mth.square(middleX - worldX) + Mth.square(middleZ - worldZ));
            double height_bili = Mth.clamp((220 - offset) / 50, 0, 1);
            double height_plain = 40 + (0.5 + noise.getValue(worldX, 0, worldZ)) * 10;
            int height = (int) (height_plain * (1 - height_bili) + height_bili * 52);
            if (worldY > height) {
                return AIR;
            } else if (worldY > height - 1) {
                return GRASS_BLOCK;
            } else if (worldY > height - 5) {
                return DIRT;
            }
            return STONE;
        };
    }
}
