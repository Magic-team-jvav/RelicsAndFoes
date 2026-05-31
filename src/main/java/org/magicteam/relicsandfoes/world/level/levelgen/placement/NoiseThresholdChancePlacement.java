package org.magicteam.relicsandfoes.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.magicteam.relicsandfoes.init.RAFDimensions;

public class NoiseThresholdChancePlacement extends RepeatingPlacement {
    public static final MapCodec<NoiseThresholdChancePlacement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.fieldOf("noise_level").forGetter(p -> p.noiseLevel),
            Codec.FLOAT.fieldOf("below_noise").forGetter(p -> p.belowNoise),
            Codec.FLOAT.fieldOf("above_noise").forGetter(p -> p.aboveNoise)
    ).apply(instance, NoiseThresholdChancePlacement::new));

    private final double noiseLevel;
    private final float belowNoise;
    private final float aboveNoise;

    private NormalNoise noise;

    private NoiseThresholdChancePlacement(double noiseLevel, float belowNoise, float aboveNoise) {
        this.noiseLevel = noiseLevel;
        this.belowNoise = belowNoise;
        this.aboveNoise = aboveNoise;
    }

    public static NoiseThresholdChancePlacement of(double noiseLevel, float belowNoise, float aboveNoise) {
        return new NoiseThresholdChancePlacement(noiseLevel, belowNoise, aboveNoise);
    }

    @Override
    protected int count(RandomSource random, BlockPos pos) {
        if (noise == null) {
            this.noise = NormalNoise.create(RandomSource.create(random.nextLong()), -9, 1, 0, 1);
        }
        double value = noise.getValue(pos.getX(), pos.getY(), pos.getZ());
        float chance = value < noiseLevel ? belowNoise : aboveNoise;
        int extra = (int) chance;
        if (random.nextFloat() < chance - extra) {
            return extra + 1;
        }
        return extra;
    }

    @Override
    public PlacementModifierType<?> type() {
        return RAFDimensions.NOISE_THRESHOLD_CHANCE_PLACEMENT.get();
    }
}
