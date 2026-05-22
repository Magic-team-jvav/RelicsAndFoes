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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.magicteam.relicsandfoes.init.RAFDimensions;
import org.magicteam.relicsandfoes.world.level.biome.RelicLandBiomeSource;

public class RelicLandChunkGenerator extends NoiseBasedChunkGenerator {
    public static final MapCodec<RelicLandChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(RelicLandChunkGenerator::getBiomeSource),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(RelicLandChunkGenerator::generatorSettings)
    ).apply(instance, instance.stable(RelicLandChunkGenerator::new)));
    protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
    protected static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.defaultBlockState();
    protected static final BlockState DIRT = Blocks.DIRT.defaultBlockState();
    protected static final BlockState STONE = Blocks.STONE.defaultBlockState();

    protected ImprovedNoise noiseGen;

    public RelicLandChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource, settings);
        if (!(biomeSource instanceof RelicLandBiomeSource)) {
            throw new IllegalArgumentException("The BiomeSource of RelicLandChunkGenerator must is instance of RelicLandBiomeSource");
        }
    }

    @Override
    protected MapCodec<RelicLandChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    protected NoiseChunk createNoiseChunk(ChunkAccess chunk, StructureManager structureManager, Blender blender, RandomState random) {
        NoiseChunk noiseChunk = super.createNoiseChunk(chunk, structureManager, blender, random);
        setupFiller(noiseChunk, chunk.getPos());
        return noiseChunk;
    }

    public void setupFiller(NoiseChunk noiseChunk, ChunkPos chunkPos) {
        if (noiseGen == null) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) return;
            RandomSource random = RandomSource.create(server.getWorldData().worldGenOptions().seed());
            this.noiseGen = new ImprovedNoise(random);
        }
        RelicLandBiomeSource biomeSource = (RelicLandBiomeSource) this.biomeSource;
        int baseX = chunkPos.getMinBlockX();
        int baseZ = chunkPos.getMinBlockZ();

        // Precompute surface height per XZ column: >=0 normal, <0 river (h = -(val+1))
        int[] heights = new int[256];

        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int wx = baseX + dx;
                int wz = baseZ + dz;
                int idx = (dx << 4) | dz;

                int biomeX = (wx >> 11 << 11) + 1024;
                int biomeZ = (wz >> 11 << 11) + 1024;
                double biomeDistanceSqr = Mth.lengthSquared(biomeX - wx, biomeZ - wz);
                Holder<Biome> biome = biomeDistanceSqr <= 800 * 800 ? biomeSource.selectBiome(biomeX, biomeZ) : biomeSource.getDefaultBiome();

                if (biome.is(RAFDimensions.Biomez.THE_PEACH_BLOSSOM_VALE)) {
                    heights[idx] = computePeachValeHeight(Math.sqrt(biomeDistanceSqr), wx, wz);
                } else {
                    heights[idx] = computeSunkenExpanseHeight(wx, wz);
                }
            }
        }

        noiseChunk.blockStateRule = context -> {
            int dx = context.blockX() - baseX;
            int dz = context.blockZ() - baseZ;
            int val = heights[(dx << 4) | dz];
            boolean isRiver = val < 0;
            int h = isRiver ? ~val : val;
            int worldY = context.blockY();

            if (worldY > h) return AIR;
            if (isRiver && worldY == h) return Blocks.WATER.defaultBlockState();
            if (worldY == h) return GRASS_BLOCK;
            if (worldY > h - 4) return DIRT;
            return STONE;
        };
    }

    /// Returns encoded height: >=0 normal, <0 river (decode: h = ~val)
    private int computePeachValeHeight(double jvli, int worldX, int worldZ) {
        double r = 500 + (0.5 + noise(worldX / 180.0 + 3548, worldZ / 360.0 - 9575) * 0.5) * 100;
        double r2 = 600 + (0.5 + noise(worldX / 320.0, worldZ / 320.0) * 0.5) * 200;
        double r3 = 145;

        double height_bili = Mth.clamp((r - jvli) / 400, 0, 1);
        double height_bili2 = Mth.clamp((r2 - jvli) / 400, 0, 1);
        double height_bili3 = Mth.clamp((r3 - jvli) / 50, 0, 1);

        double height_plain = 40 + ((0.5 + noise(worldX / 300.0, worldZ / 300.0) * 0.5) * 10
                + (0.5 + noise(worldX / 80.0, worldZ / 80.0) * 0.5) * 2) * (1 - height_bili);

        double mn_a = noise(worldX / 100.0 + 200, worldZ / 100.0 + 200);
        double mn_b = noise(worldX / 70.0 + 400, worldZ / 70.0 + 400);
        double mn_c = noise(worldX / 55.0 + 600, worldZ / 55.0 + 600);
        double mn_detail = noise(worldX / 50.0 + 75, worldZ / 50.0 - 155);
        double pa = Math.max(0, Math.pow(Math.abs(mn_a * 2.0), 2.5) + mn_detail * 0.2 - 0.12) * 55;
        double pb = Math.max(0, Math.pow(Math.abs(mn_b * 1.8), 2.5) + mn_detail * 0.2 - 0.12) * 45;
        double pc = Math.max(0, Math.pow(Math.abs(mn_c * 1.5), 2.5) + mn_detail * 0.2 - 0.12) * 40;

        double river_noise = noise(worldX / 150.0, worldZ / 150.0);

        double mountainFade = Mth.clamp((Math.abs(river_noise) - 0.03) / 0.07, 0, 1);
        double height_mountain = Math.max(Math.max(pa, pb), pc) * mountainFade;

        double surface = 28 * height_bili + height_plain + height_mountain * (height_bili2 - height_bili3);
        int h = (int) surface;
        boolean isRiver = river_noise < 0.03 && river_noise > -0.03;
        return isRiver ? ~h : h;
    }

    private int computeSunkenExpanseHeight(int worldX, int worldZ) {
        int middleX = (worldX + 1024) >> 11 << 11;
        int middleZ = (worldZ + 1024) >> 11 << 11;
        double distance = Mth.length(middleX - worldX, middleZ - worldZ);
        double r = 220;
        double heightRatio = Mth.clamp((r - distance) / 50, 0, 1);
        double heightBase = 46 + noise(worldX / 300.0, worldZ / 300.0) * 5 + noise(worldX / 80.0, worldZ / 80.0);
        return (int) (heightBase * (1 - heightRatio) + heightRatio * 52);
    }

    private double noise(double x, double z) {
        // y = -yo cancels the y offset → deltaY=0 → true 2D noise
        return noiseGen.noise(x, -noiseGen.yo, z);
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
