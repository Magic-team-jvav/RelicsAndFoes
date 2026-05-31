package org.magicteam.relicsandfoes.util.valueproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviderType;
import org.magicteam.relicsandfoes.init.RAFDimensions;

public class FloatChanceIntProvider extends IntProvider {
    public static final MapCodec<FloatChanceIntProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.floatRange(Mth.EPSILON, 256).fieldOf("chance").forGetter(p -> p.chance),
            ExtraCodecs.POSITIVE_INT.lenientOptionalFieldOf("max_inclusive", 256).forGetter(p -> p.maxInclusive)
    ).apply(instance, FloatChanceIntProvider::new));

    private final float chance;
    private final int maxInclusive;

    private FloatChanceIntProvider(float chance, int maxInclusive) {
        this.chance = chance;
        this.maxInclusive = maxInclusive;
    }

    public static FloatChanceIntProvider of(float chance, int maxInclusive) {
        return new FloatChanceIntProvider(chance, maxInclusive);
    }

    public static FloatChanceIntProvider of(float chance) {
        return of(chance, 256);
    }

    @Override
    public int sample(RandomSource random) {
        int i = (int) chance;
        float v = chance - i;
        if (random.nextFloat() < v) {
            ++i;
        }
        return i;
    }

    @Override
    public int getMinValue() {
        return 0;
    }

    @Override
    public int getMaxValue() {
        return maxInclusive;
    }

    @Override
    public IntProviderType<?> getType() {
        return RAFDimensions.FLOAT_CHANCE_INT_PROVIDER.get();
    }
}
