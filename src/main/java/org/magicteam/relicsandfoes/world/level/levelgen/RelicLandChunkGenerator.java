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
    protected static final BlockState WATER = Blocks.WATER.defaultBlockState();
    protected static final BlockState SAND = Blocks.SAND.defaultBlockState();
    protected static final BlockState COARSE_DIRT = Blocks.COARSE_DIRT.defaultBlockState();
    protected static final BlockState SNOW_BLOCK = Blocks.SNOW_BLOCK.defaultBlockState();

    // Bit-packed column: [9:0]=height, [12:10]=surface, [14:13]=waterType
    private static final int HEIGHT_MASK = 0x3FF;
    private static final int SURF_MASK = 7;
    private static final int SURF_SHIFT = 10;
    private static final int SURF_GRASS = 0;
    private static final int SURF_WATER = 1;
    private static final int SURF_SAND = 2;
    private static final int SURF_COARSE = 3;
    private static final int SURF_STONE = 4;
    private static final int SURF_SNOW = 5;
    private static final int WATER_SHIFT = 13;
    private static final int WATER_MASK = 3;
    private static final int WATER_NONE = 0;
    private static final int WATER_TO_15 = 1;
    private static final int WATER_TO_37 = 2;

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

        // Precompute bit-packed column data
        int[] columns = new int[256];

        // Biome is constant per chunk — determine once outside the loop
        int cx = baseX + 8, cz = baseZ + 8;
        int biomeCX = (cx >> 11 << 11) + 1024, biomeCZ = (cz >> 11 << 11) + 1024;
        Holder<Biome> biome = Mth.lengthSquared(biomeCX - cx, biomeCZ - cz) <= 800 * 800
                ? biomeSource.selectBiome(biomeCX, biomeCZ) : biomeSource.getDefaultBiome();

        int biomeType; // 0-7
        if (biome.is(RAFDimensions.Biomez.THE_PEACH_BLOSSOM_VALE)) biomeType = 0;
        else if (biome.is(RAFDimensions.Biomez.THE_SEA_OF_FALLING_STARS)) biomeType = 1;
        else if (biome.is(RAFDimensions.Biomez.THE_THORNY_DREADLANDS)) biomeType = 2;
        else if (biome.is(RAFDimensions.Biomez.THE_AZURE_SEA)) biomeType = 3;
        else if (biome.is(RAFDimensions.Biomez.THE_MISTY_SNOWY_PEAKS)) biomeType = 4;
        else if (biome.is(RAFDimensions.Biomez.THE_RUST_SILENT_CITY)) biomeType = 5;
        else if (biome.is(RAFDimensions.Biomez.THE_FOREST_OF_DUSK)) biomeType = 6;
        else biomeType = 7;

        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int wx = baseX + dx;
                int wz = baseZ + dz;
                double dist = Mth.length(biomeCX - wx, biomeCZ - wz);

                columns[(dx << 4) | dz] = switch (biomeType) {
                    case 0 -> computePeachValeColumn(dist, wx, wz);
                    case 1 -> computeSeaOfFallingStarsColumn(dist, wx, wz);
                    case 2 -> computeThornyDreadlandsColumn(dist, wx, wz);
                    case 3 -> computeAzureSeaColumn(dist, wx, wz);
                    case 4 -> computeMistySnowyPeaksColumn(dist, wx, wz);
                    case 5 -> computeRustSilentCityColumn(dist, wx, wz);
                    case 6 -> computeForestOfDuskColumn(dist, wx, wz);
                    default -> computeSunkenExpanseColumn(wx, wz);
                };
            }
        }

        noiseChunk.blockStateRule = context -> {
            int col = columns[((context.blockX() - baseX) << 4) | (context.blockZ() - baseZ)];
            int h = col & HEIGHT_MASK;
            int surf = (col >> SURF_SHIFT) & SURF_MASK;
            int waterType = (col >> WATER_SHIFT) & WATER_MASK;
            int worldY = context.blockY();

            if (waterType != WATER_NONE) {
                int waterMax = waterType == WATER_TO_15 ? 15 : 37;
                if (worldY > waterMax) return AIR;
                if (worldY > h) return WATER;
            }

            if (worldY > h) return AIR;
            if (worldY == h || (surf == SURF_SNOW && worldY == h - 1))
                return switch (surf) {
                    case SURF_WATER -> WATER;
                    case SURF_SAND -> SAND;
                    case SURF_COARSE -> COARSE_DIRT;
                    case SURF_STONE -> STONE;
                    case SURF_SNOW -> SNOW_BLOCK;
                    default -> GRASS_BLOCK;
                };
            if (worldY > h - 4) return DIRT;
            return STONE;
        };
    }

    private int computePeachValeColumn(double dist, int worldX, int worldZ) {
        double r = 500 + (0.5 + noise(worldX / 180.0 + 3548, worldZ / 360.0 - 9575) * 0.5) * 100;
        double r2 = 600 + (0.5 + noise(worldX / 320.0, worldZ / 320.0) * 0.5) * 200;
        double r3 = 145;

        double heightRatio = Mth.clamp((r - dist) / 400, 0, 1);
        double heightRatio2 = Mth.clamp((r2 - dist) / 400, 0, 1);
        double heightRatio3 = Mth.clamp((r3 - dist) / 50, 0, 1);

        double heightBase = 40 + ((0.5 + noise(worldX / 300.0, worldZ / 300.0) * 0.5) * 10
                + (0.5 + noise(worldX / 80.0, worldZ / 80.0) * 0.5) * 2) * (1 - heightRatio);

        double mnA = noise(worldX / 100.0 + 200, worldZ / 100.0 + 200);
        double mnB = noise(worldX / 70.0 + 400, worldZ / 70.0 + 400);
        double mnC = noise(worldX / 55.0 + 600, worldZ / 55.0 + 600);
        double mnDetail = noise(worldX / 50.0 + 75, worldZ / 50.0 - 155);
        double pa = Math.max(0, Math.pow(Math.abs(mnA * 2.0), 2.5) + mnDetail * 0.2 - 0.12) * 55;
        double pb = Math.max(0, Math.pow(Math.abs(mnB * 1.8), 2.5) + mnDetail * 0.2 - 0.12) * 45;
        double pc = Math.max(0, Math.pow(Math.abs(mnC * 1.5), 2.5) + mnDetail * 0.2 - 0.12) * 40;

        double riverNoise = noise(worldX / 150.0, worldZ / 150.0);
        double mountainFade = Mth.clamp((Math.abs(riverNoise) - 0.03) / 0.07, 0, 1);
        double heightMountain = Math.max(Math.max(pa, pb), pc) * mountainFade;

        int h = (int) (28 * heightRatio + heightBase + heightMountain * (heightRatio2 - heightRatio3));
        int surf = (riverNoise < 0.03 && riverNoise > -0.03) ? SURF_WATER : SURF_GRASS;
        return h | (surf << SURF_SHIFT);
    }

    /** Returns true when mountain contribution < 5 — flat ground suitable for structures. */
    public boolean isPeachValeValleyFloor(int worldX, int worldZ, int biomeX, int biomeZ) {
        double dist = Math.sqrt(Mth.lengthSquared(biomeX - worldX, biomeZ - worldZ));

        double r2 = 600 + (0.5 + noise(worldX / 320.0, worldZ / 320.0) * 0.5) * 200;
        double ratio2 = Mth.clamp((r2 - dist) / 400, 0, 1);
        double ratio3 = Mth.clamp((145 - dist) / 50, 0, 1);

        double mnDetail = noise(worldX / 50.0 + 75, worldZ / 50.0 - 155);
        double pa = Math.max(0, Math.pow(Math.abs(noise(worldX / 100.0 + 200, worldZ / 100.0 + 200) * 2.0), 2.5) + mnDetail * 0.2 - 0.12) * 55;
        double pb = Math.max(0, Math.pow(Math.abs(noise(worldX / 70.0 + 400, worldZ / 70.0 + 400) * 1.8), 2.5) + mnDetail * 0.2 - 0.12) * 45;
        double pc = Math.max(0, Math.pow(Math.abs(noise(worldX / 55.0 + 600, worldZ / 55.0 + 600) * 1.5), 2.5) + mnDetail * 0.2 - 0.12) * 40;

        double riverNoise = noise(worldX / 150.0, worldZ / 150.0);
        double fade = Mth.clamp((Math.abs(riverNoise) - 0.03) / 0.07, 0, 1);

        return Math.max(Math.max(pa, pb), pc) * fade * (ratio2 - ratio3) < 5;
    }

    /// Rugged terrain: warped noise mountains on plain base, coarse dirt patches.
    private int computeThornyDreadlandsColumn(double dist, int worldX, int worldZ) {
        double r = 650 + (0.5 + noise(worldX / 180.0 + 3548, worldZ / 360.0 - 9575) * 0.5) * 100;
        double r2 = 600 + (0.5 + noise(worldX / 320.0, worldZ / 320.0) * 0.5) * 200;
        double r3 = 145;

        double heightRatio = Mth.clamp((r - dist) / 200, 0, 1);
        double heightRatio2 = Mth.clamp((r2 - dist) / 400, 0, 1);
        double heightRatio3 = Mth.clamp((r3 - dist) / 50, 0, 1);

        double heightBase = 40 + ((0.5 + noise(worldX / 300.0, worldZ / 300.0) * 0.5) * 10
                + (0.5 + noise(worldX / 80.0, worldZ / 80.0) * 0.5) * 2) * (1 - heightRatio);

        double n1 = noise(worldX / 80.0 + 25, worldZ / 80.0 - 205);
        double partA = Math.pow(n1 + 1, 1.5) * 2;

        double baseN = noise(worldX / 80.0 + 75, worldZ / 80.0 - 155);
        double partB = Math.pow(noise(baseN * 3, baseN * 3) + 1, 1.5);

        double height_mountain = (partA + partB) * 8;

        int h = (int) (heightBase + height_mountain * (heightRatio2 - heightRatio3));
        int surf = noise(worldX / 50.0 + 158, worldZ / 50.0 - 756) > 0 ? SURF_COARSE : SURF_GRASS;
        return h | (surf << SURF_SHIFT);
    }

    /// Deep basin: y=6 inside, eroded rim, water to y=15.
    private int computeSeaOfFallingStarsColumn(double dist, int worldX, int worldZ) {
        double heightBase = 40 + (0.5 + noise(worldX / 300.0, worldZ / 300.0) * 0.5) * 10
                + (0.5 + noise(worldX / 80.0, worldZ / 80.0) * 0.5) * 2;

        double r = 670 + (0.5 + 0.5 * noise(worldX / 360.0 + 3548, worldZ / 360.0 - 9575)) * 80
                + (0.5 + 0.5 * noise(worldX / 80.0 + 1124, worldZ / 80.0 - 7521)) * 40;

        double heightRatio = Math.min((r - dist) / 800, 0) * 0.5;

        int h;
        if (heightRatio < 0) {
            double erosion = Math.max(0.005 + heightRatio, 0);
            double noiseTerm = Math.pow(0.5 + noise(worldX / 15.0, worldZ / 15.0) * 0.5, 1.8) * 35 + 8;
            h = (int) (heightBase * (1 - erosion * noiseTerm));
        } else {
            h = 6;
        }

        int waterType = h < 16 ? WATER_TO_15 : WATER_NONE;
        int surf = h < 10 ? SURF_SAND : SURF_GRASS;
        return h | (surf << SURF_SHIFT) | (waterType << WATER_SHIFT);
    }

    /// Shallow basin lake: height lerps from plain to 10, water to y=37, sandy shore.
    private int computeAzureSeaColumn(double dist, int worldX, int worldZ) {
        double r = 600 + (0.5 + 0.5 * noise(worldX / 180.0 + 3548, worldZ / 360.0 - 9575)) * 150;
        double r2 = 650 + (0.5 + noise(worldX / 320.0, worldZ / 320.0) * 0.5) * 120
                + (0.5 + noise(worldX / 50.0, worldZ / 50.0) * 0.5) * 30;

        double heightRatio = Mth.clamp((r - dist) / 800, 0, 1) * 0.5;
        double heightRatio2 = Mth.clamp((r2 - dist) / 650, 0, 1) * 0.5;
        double biliSum = heightRatio + heightRatio2;

        double heightBase = 40 + (0.5 + noise(worldX / 300.0, worldZ / 300.0) * 0.5) * 10
                + (0.5 + noise(worldX / 80.0, worldZ / 80.0) * 0.5) * 2;

        double heightMountain = 10;
        int h = (int) (heightBase * (1 - biliSum) + heightMountain * biliSum);

        int waterType = h < 38 ? WATER_TO_37 : WATER_NONE;
        int surf = biliSum >= 0.1 ? SURF_SAND : SURF_GRASS;
        return h | (surf << SURF_SHIFT) | (waterType << WATER_SHIFT);
    }

    /// Tall peaks (up to y=240): grass/stone/snow zones with domain-warped radius.
    private int computeMistySnowyPeaksColumn(double dist, int worldX, int worldZ) {
        double heightBase = 40 + (0.5 + noise(worldX / 300.0, worldZ / 300.0) * 0.5) * 10
                + (0.5 + noise(worldX / 80.0, worldZ / 80.0) * 0.5) * 2;

        // v.r: domain-warped noise radius
        double warpX = Math.pow(1 + noise(worldX / 40.0 + 156, worldZ / 40.0 - 752), 1.4) * 0.03;
        double warpZ = Math.pow(1 + noise(worldX / 40.0 + 256, worldZ / 40.0 - 248), 1.4) * 0.03;
        double warpedNoise = noise(worldX / 320.0 + 8752 + warpX, worldZ / 180.0 - 4569 + warpZ);
        double r = 450 + Math.pow(1 + warpedNoise, 1.25) * 0.55 * 350;

        // v.r2
        double r2 = 650 + (0.5 + noise(worldX / 320.0, worldZ / 320.0) * 0.5) * 120
                + (0.5 + noise(worldX / 50.0, worldZ / 50.0) * 0.5) * 30;

        double heightRatio = Mth.clamp((r - dist) / 800, 0, 1);
        double heightRatio2 = Mth.clamp((r2 - dist) / 800, 0, 1) * 0.666;
        double biliMax = Math.max(heightRatio, heightRatio2);

        int h = (int) (heightBase * (1 - biliMax) + 240 * biliMax);

        // Stone exposure line and snow line
        double stoneH = 112 + noise(worldX / 45.0 + 124, worldZ / 45.0 - 789) * 30
                + noise(worldX / 18.0 + 721, worldZ / 18.0 - 319) * 10;
        double snowH = 160 + noise(worldX / 18.0 + 721, worldZ / 18.0 - 319) * 6;

        int surf;
        if (h > snowH) surf = SURF_SNOW;
        else if (h > stoneH) surf = SURF_STONE;
        else surf = SURF_GRASS;

        return h | (surf << SURF_SHIFT);
    }

    /// Bumpy plateau with gentle hills (0-9 blocks), ring-based placement.
    private int computeRustSilentCityColumn(double dist, int worldX, int worldZ) {
        double r = 650 + (0.5 + noise(worldX / 180.0 + 3548, worldZ / 360.0 - 9575) * 0.5) * 100;
        double r2 = 700 + (0.5 + noise(worldX / 320.0, worldZ / 320.0) * 0.5) * 100;
        double r3 = 145;

        double heightRatio = Mth.clamp((r - dist) / 200, 0, 1);
        double heightRatio2 = Mth.clamp((r2 - dist) / 100, 0, 1);
        double heightRatio3 = Mth.clamp((r3 - dist) / 50, 0, 1);

        double heightBase = 40 + ((0.5 + noise(worldX / 300.0, worldZ / 300.0) * 0.5) * 10
                + (0.5 + noise(worldX / 80.0, worldZ / 80.0) * 0.5) * 2) * (1 - heightRatio);

        double heightMountain = (0.5 + noise(worldX / 140.0, worldZ / 140.0) * 0.5) * 7
                + (0.5 + noise(worldX / 55.0, worldZ / 55.0) * 0.5) * 2;

        return (int) (heightBase + heightMountain * (heightRatio2 - heightRatio3));
    }

    /// Steep forested peaks: domain-warped radius, lerp from plain to y=160.
    private int computeForestOfDuskColumn(double dist, int worldX, int worldZ) {
        double heightBase = 40 + (0.5 + noise(worldX / 300.0, worldZ / 300.0) * 0.5) * 10
                + (0.5 + noise(worldX / 80.0, worldZ / 80.0) * 0.5) * 2;

        // v.r: domain-warped radius
        double warpX = Math.pow(1 + noise(worldX / 70.0 + 245, worldZ / 35.0 - 356), 1.4) * 0.05;
        double warpZ = Math.pow(1 + noise(worldX / 70.0 + 197, worldZ / 35.0 - 856), 1.4) * 0.05;
        double warpedNoise = noise(worldX / 180.0 + 3548 + warpX, worldZ / 360.0 - 9575 + warpZ);
        double r = 350 + Math.pow(1 + warpedNoise, 1.12) * 0.4 * 250;

        // v.r2
        double r2 = 650 + (0.5 + noise(worldX / 320.0, worldZ / 320.0) * 0.5) * 120
                + (0.5 + noise(worldX / 50.0, worldZ / 50.0) * 0.5) * 30;

        double heightRatio = Mth.clamp((r - dist) / 350, 0, 1);
        double heightRatio2 = Mth.clamp((r2 - dist) / 620, 0, 1) * 0.35;
        double biliSum = heightRatio + heightRatio2;

        return (int) (heightBase * (1 - biliSum) + 160 * biliSum);
    }

    private int computeSunkenExpanseColumn(int worldX, int worldZ) {
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
