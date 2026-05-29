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
    private static final NormalNoise NOISE = NormalNoise.create(RandomSource.create(260521), -9, 1, 0, 1);

    private final double noiseLevel;
    private final float belowNoise;
    private final float aboveNoise;

    public NoiseThresholdChancePlacement(double noiseLevel, float belowNoise, float aboveNoise) {
        this.noiseLevel = noiseLevel;
        this.belowNoise = belowNoise;
        this.aboveNoise = aboveNoise;
    }

    @Override
    protected int count(RandomSource random, BlockPos pos) {
        double value = NOISE.getValue(pos.getX(), pos.getY(), pos.getZ());
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
