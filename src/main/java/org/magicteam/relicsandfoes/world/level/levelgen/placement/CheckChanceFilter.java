package org.magicteam.relicsandfoes.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.magicteam.relicsandfoes.init.RAFDimensions;

public class CheckChanceFilter extends PlacementFilter {
    public static final MapCodec<CheckChanceFilter> CODEC = Codec.floatRange(Mth.EPSILON, 1).fieldOf("chance").xmap(CheckChanceFilter::new, f -> f.chance);
    private final float chance;

    private CheckChanceFilter(float chance) {
        this.chance = chance;
    }

    public static CheckChanceFilter of(float chance) {
        return new CheckChanceFilter(chance);
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        boolean b = random.nextFloat() < chance;
        return b;
    }

    @Override
    public PlacementModifierType<?> type() {
        return RAFDimensions.CHECK_CHANCE_FILTER.get();
    }
}
