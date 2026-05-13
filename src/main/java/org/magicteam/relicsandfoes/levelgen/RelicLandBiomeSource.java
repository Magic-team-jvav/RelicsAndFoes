package org.magicteam.relicsandfoes.levelgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.DensityFunction;
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
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        int i = QuartPos.toBlock(x);
        int k = QuartPos.toBlock(z);
        int middleX = (i + 1024) >> 11 << 11;
        int middleZ = (k + 1024) >> 11 << 11;
        if (Mth.square(middleX - i) + Mth.square(middleZ - k) > 800 * 800) {
            int j = QuartPos.toBlock(y);
            DensityFunction.SinglePointContext context = new DensityFunction.SinglePointContext(i, j, k);
            double continentalness = sampler.continentalness().compute(context);
            double temperature = sampler.temperature().compute(context);
            double weirdness = sampler.weirdness().compute(context);
            double erosion = sampler.erosion().compute(context);
            if (erosion < 0.1) { // 起伏大
                if (continentalness > -0.1) { // 大陆
                    if (weirdness < 0) { // 奇异
                        return thePeachBlossomVale;
                    }
                    if (continentalness > 0.15) { // 高山
                        return theThornyDreadlands;
                    }
                    if (temperature < 0) { // 寒冷
                        return theMistySnowyPeaks;
                    } else if (temperature > 0.3) { // 炎热
                        return theRustSilentCity;
                    }
                    return theForestOfDusk;
                } else { // 海洋
                    if (weirdness < 0) { // 奇异
                        return theSeaOfFallingStars;
                    }
                    return theAzureSea;
                }
            }
        }
        return theSunkenPlains;
    }
}
