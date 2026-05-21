package org.magicteam.relicsandfoes.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.magicteam.relicsandfoes.init.RAFDimensions;

import java.util.stream.Stream;

public class NoiseThresholdChancePlacement extends PlacementModifier {
    public static final MapCodec<NoiseThresholdChancePlacement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.fieldOf("noise_level").forGetter(p -> p.noiseLevel),
            ExtraCodecs.POSITIVE_FLOAT.fieldOf("below_noise").forGetter(p -> p.belowNoise),
            ExtraCodecs.POSITIVE_FLOAT.fieldOf("above_noise").forGetter(p -> p.aboveNoise)
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
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        double value = NOISE.getValue(pos.getX(), pos.getY(), pos.getZ());
        if (random.nextFloat() < (value < noiseLevel ? belowNoise : aboveNoise)) {
            return Stream.of(pos);
        }
        return Stream.empty();
    }

    @Override
    public PlacementModifierType<?> type() {
        return RAFDimensions.NOISE_THRESHOLD_CHANCE_PLACEMENT.get();
    }
}
