package org.magicteam.relicsandfoes.world.level.levelgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
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
        if (noise == null) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) return;
            RandomSource random = RandomSource.create(server.getWorldData().worldGenOptions().seed());
            double[] amplitudes = new double[random.nextInt(3, 6)];
            for (int j = 0; j < amplitudes.length; j++) {
                amplitudes[j] = random.nextDouble();
            }
            this.noise = NormalNoise.create(random, -9, amplitudes);
        }
        noiseChunk.blockStateRule = context -> {
            int worldX = context.blockX();
            int worldY = context.blockY();
            int worldZ = context.blockZ();
            int middleX = (worldX + 1024) >> 11 << 11;
            int middleZ = (worldZ + 1024) >> 11 << 11;
            double offset = Mth.length(middleX - worldX, middleZ - worldZ);
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

    @Override
    public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState random, ChunkAccess chunk) {
        super.buildSurface(level, structureManager, random, chunk);

        // 朝圣之路
        ChunkPos pos = chunk.getPos();
        int mx = pos.x % 128;
        int regionX = mx < 0 ? mx + 128 : mx;
        int mz = pos.z % 128;
        int regionZ = mz < 0 ? mz + 128 : mz;
        int sectionX = 0, sizeX;
        int sectionZ = 0, sizeZ;
        if ((regionX >= 7 && regionX <= 120) && (mz == 0 || mz == -1)) {
            if (regionX == 7) {
                sizeX = 8;
            } else if (regionX == 120) {
                sizeX = 11;
            } else {
                sizeX = 16;
            }
            sectionZ = mz == 0 ? 0 : 13;
            sizeZ = mz == 0 ? 4 : 3;
        } else if ((regionZ >= 6 && regionZ <= 119) && (mx == 0 || mx == -1)) {
            sectionX = mx == 0 ? 0 : 13;
            sizeX = mx == 0 ? 4 : 3;
            sizeZ = regionZ == 119 ? 1 : 16;
        } else {
            return;
        }
        WorldgenRandom worldgenRandom = new WorldgenRandom(RandomSource.create(260520));
        worldgenRandom.setLargeFeatureSeed(level.getSeed(), pos.x, pos.z);
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        for (int x = 0; x < sizeX; x++) {
            for (int z = 0; z < sizeZ; z++) {
                int i = worldgenRandom.nextInt(4);
                if (i == 0) continue;
                int bx = pos.getBlockX(sectionX + x);
                int bz = pos.getBlockZ(sectionZ + z);
                blockPos.set(bx, level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, bx, bz) - 1, bz);
                if (i == 1) {
                    level.setBlock(blockPos, Blocks.TUFF_BRICKS.defaultBlockState(), Block.UPDATE_CLIENTS);
                } else if (i == 2) {
                    level.setBlock(blockPos, Blocks.POLISHED_TUFF.defaultBlockState(), Block.UPDATE_CLIENTS);
                } else { // i == 3
                    level.setBlock(blockPos, Blocks.TUFF.defaultBlockState(), Block.UPDATE_CLIENTS);
                }
            }
        }
    }
}
