package org.magicteam.relicsandfoes.worldgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

public record FlipLessThanY(DensityFunction input, double yInclusive) implements DensityFunction {
    public static final MapCodec<FlipLessThanY> DATA_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DensityFunction.DIRECT_CODEC.fieldOf("input").forGetter(FlipLessThanY::input),
            DensityFunctions.NOISE_VALUE_CODEC.fieldOf("y_inclusive").forGetter(FlipLessThanY::yInclusive)
    ).apply(instance, FlipLessThanY::new));
    public static final KeyDispatchDataCodec<FlipLessThanY> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

    @Override
    public double compute(FunctionContext context) {
        double density = input.compute(context);
        if (density < 0 && context.blockY() <= yInclusive) {
            int i = context.blockX();
            int k = context.blockZ();
            int middleX = (i + 1024) >> 11 << 11;
            int middleZ = (k + 1024) >> 11 << 11;
            if (Mth.square(middleX - i) + Mth.square(middleZ - k) <= 600 * 600) {
                return -density;
            }
        }
        return density;
    }

    @Override
    public void fillArray(double[] array, ContextProvider contextProvider) {
        contextProvider.fillAllDirectly(array, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new FlipLessThanY(input.mapAll(visitor), yInclusive));
    }

    @Override
    public double minValue() {
        return input.minValue();
    }

    @Override
    public double maxValue() {
        return input.maxValue();
    }

    @Override
    public KeyDispatchDataCodec<FlipLessThanY> codec() {
        return CODEC;
    }
}
