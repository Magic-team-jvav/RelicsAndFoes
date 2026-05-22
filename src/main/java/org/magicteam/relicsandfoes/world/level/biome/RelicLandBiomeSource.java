package org.magicteam.relicsandfoes.world.level.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.magicteam.relicsandfoes.init.RAFDimensions;

import java.util.stream.Stream;

public class RelicLandBiomeSource extends BiomeSource {
    public static final MapCodec<RelicLandBiomeSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RegistryOps.retrieveElement(RAFDimensions.Biomez.THE_SUNKEN_EXPANSE),
            RegistryOps.retrieveElement(RAFDimensions.Biomez.THE_MISTY_SNOWY_PEAKS),
            RegistryOps.retrieveElement(RAFDimensions.Biomez.THE_RUST_SILENT_CITY),
            RegistryOps.retrieveElement(RAFDimensions.Biomez.THE_AZURE_SEA),
            RegistryOps.retrieveElement(RAFDimensions.Biomez.THE_THORNY_DREADLANDS),
            RegistryOps.retrieveElement(RAFDimensions.Biomez.THE_PEACH_BLOSSOM_VALE),
            RegistryOps.retrieveElement(RAFDimensions.Biomez.THE_SEA_OF_FALLING_STARS),
            RegistryOps.retrieveElement(RAFDimensions.Biomez.THE_FOREST_OF_DUSK)
    ).apply(instance, instance.stable(RelicLandBiomeSource::new)));

    private final Holder<Biome> theSunkenPlains;
    private final Holder<Biome> theMistySnowyPeaks;
    private final Holder<Biome> theRustSilentCity;
    private final Holder<Biome> theAzureSea;
    private final Holder<Biome> theThornyDreadlands;
    private final Holder<Biome> thePeachBlossomVale;
    private final Holder<Biome> theSeaOfFallingStars;
    private final Holder<Biome> theForestOfDusk;

    private final SimpleWeightedRandomList<Holder<Biome>> otherBiomes;
    private final Long2ObjectOpenHashMap<Holder<Biome>> biomeCache = new Long2ObjectOpenHashMap<>();
    private long worldSeed;
    private boolean seedReady;

    public RelicLandBiomeSource(
            Holder<Biome> theSunkenPlains,
            Holder<Biome> theMistySnowyPeaks,
            Holder<Biome> theRustSilentCity,
            Holder<Biome> theAzureSea,
            Holder<Biome> theThornyDreadlands,
            Holder<Biome> thePeachBlossomVale,
            Holder<Biome> theSeaOfFallingStars,
            Holder<Biome> theForestOfDusk
    ) {
        this.theSunkenPlains = theSunkenPlains;
        this.theMistySnowyPeaks = theMistySnowyPeaks;
        this.theRustSilentCity = theRustSilentCity;
        this.theAzureSea = theAzureSea;
        this.theThornyDreadlands = theThornyDreadlands;
        this.thePeachBlossomVale = thePeachBlossomVale;
        this.theSeaOfFallingStars = theSeaOfFallingStars;
        this.theForestOfDusk = theForestOfDusk;

        this.otherBiomes = SimpleWeightedRandomList.<Holder<Biome>>builder()
                .add(theForestOfDusk, 1)
                .add(theMistySnowyPeaks, 1)
                .add(theAzureSea, 1)
                .add(thePeachBlossomVale, 3)
                .add(theThornyDreadlands, 3)
                .add(theSeaOfFallingStars, 3)
                .add(theRustSilentCity, 3)
                .build();
    }

    public static RelicLandBiomeSource create(HolderGetter<Biome> biomes) {
        return new RelicLandBiomeSource(
                biomes.getOrThrow(RAFDimensions.Biomez.THE_SUNKEN_EXPANSE),
                biomes.getOrThrow(RAFDimensions.Biomez.THE_MISTY_SNOWY_PEAKS),
                biomes.getOrThrow(RAFDimensions.Biomez.THE_RUST_SILENT_CITY),
                biomes.getOrThrow(RAFDimensions.Biomez.THE_AZURE_SEA),
                biomes.getOrThrow(RAFDimensions.Biomez.THE_THORNY_DREADLANDS),
                biomes.getOrThrow(RAFDimensions.Biomez.THE_PEACH_BLOSSOM_VALE),
                biomes.getOrThrow(RAFDimensions.Biomez.THE_SEA_OF_FALLING_STARS),
                biomes.getOrThrow(RAFDimensions.Biomez.THE_FOREST_OF_DUSK)
        );
    }

    @Override
    protected MapCodec<RelicLandBiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.of(
                theSunkenPlains,
                theMistySnowyPeaks,
                theRustSilentCity,
                theAzureSea,
                theThornyDreadlands,
                thePeachBlossomVale,
                theSeaOfFallingStars,
                theForestOfDusk
        );
    }

    @Override
    public Holder<Biome> getNoiseBiome(int qx, int qy, int qz, Climate.Sampler sampler) {
        int worldX = QuartPos.toBlock(qx);
        int worldZ = QuartPos.toBlock(qz);
        int biomeX = (worldX >> 11 << 11) + 1024;
        int biomeZ = (worldZ >> 11 << 11) + 1024;
        return selectBiome(worldX, worldZ, biomeX, biomeZ);
    }

    private void ensureSeedReady() {
        if (!seedReady) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                worldSeed = server.getWorldData().worldGenOptions().seed();
                seedReady = true;
            }
        }
    }

    public Holder<Biome> selectBiome(int worldX, int worldZ, int biomeX, int biomeZ) {
        if (Mth.lengthSquared(biomeX - worldX, biomeZ - worldZ) <= 800 * 800) {
            return selectBiome(biomeX, biomeZ);
        }
        return theSunkenPlains;
    }

    public Holder<Biome> selectBiome(int biomeX, int biomeZ) {
        long key = ChunkPos.asLong(biomeX, biomeZ);
        Holder<Biome> biome = biomeCache.get(key);
        if (biome != null) {
            return biome;
        }
        ensureSeedReady();
        long l = biomeX * 341873128712L + biomeZ * 132897987541L + worldSeed + 9872349113L;
        RandomSource random = RandomSource.create(l * l * 4234567891L + l);
        biome = otherBiomes.getRandomValue(random).orElse(theSunkenPlains);
        biomeCache.put(key, biome);
        return biome;
    }

    public Holder<Biome> getDefaultBiome() {
        return theSunkenPlains;
    }
}
