package org.magicteam.relicsandfoes.worldgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.magicteam.relicsandfoes.init.ModDimensions;

import java.util.stream.Stream;

public class RelicLandBiomeSource extends BiomeSource {
    public static final MapCodec<RelicLandBiomeSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RegistryOps.retrieveElement(ModDimensions.Biomez.THE_SUNKEN_EXPANSE),
            RegistryOps.retrieveElement(ModDimensions.Biomez.THE_MISTY_SNOWY_PEAKS),
            RegistryOps.retrieveElement(ModDimensions.Biomez.THE_RUST_SILENT_CITY),
            RegistryOps.retrieveElement(ModDimensions.Biomez.THE_AZURE_SEA),
            RegistryOps.retrieveElement(ModDimensions.Biomez.THE_THORNY_DREADLANDS),
            RegistryOps.retrieveElement(ModDimensions.Biomez.THE_PEACH_BLOSSOM_VALE),
            RegistryOps.retrieveElement(ModDimensions.Biomez.THE_SEA_OF_FALLING_STARS),
            RegistryOps.retrieveElement(ModDimensions.Biomez.THE_FOREST_OF_DUSK)
    ).apply(instance, instance.stable(RelicLandBiomeSource::new)));

    private final Holder<Biome> theSunkenPlains;
    private final Holder<Biome> theMistySnowyPeaks;
    private final Holder<Biome> theRustSilentCity;
    private final Holder<Biome> theAzureSea;
    private final Holder<Biome> theThornyDreadlands;
    private final Holder<Biome> thePeachBlossomVale;
    private final Holder<Biome> theSeaOfFallingStars;
    private final Holder<Biome> theForestOfDusk;

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
    }

    public static RelicLandBiomeSource create(HolderGetter<Biome> biomes) {
        return new RelicLandBiomeSource(
                biomes.getOrThrow(ModDimensions.Biomez.THE_SUNKEN_EXPANSE),
                biomes.getOrThrow(ModDimensions.Biomez.THE_MISTY_SNOWY_PEAKS),
                biomes.getOrThrow(ModDimensions.Biomez.THE_RUST_SILENT_CITY),
                biomes.getOrThrow(ModDimensions.Biomez.THE_AZURE_SEA),
                biomes.getOrThrow(ModDimensions.Biomez.THE_THORNY_DREADLANDS),
                biomes.getOrThrow(ModDimensions.Biomez.THE_PEACH_BLOSSOM_VALE),
                biomes.getOrThrow(ModDimensions.Biomez.THE_SEA_OF_FALLING_STARS),
                biomes.getOrThrow(ModDimensions.Biomez.THE_FOREST_OF_DUSK)
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
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        return theSunkenPlains;
    }
}
