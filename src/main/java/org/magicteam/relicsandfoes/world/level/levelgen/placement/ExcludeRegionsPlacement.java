package org.magicteam.relicsandfoes.world.level.levelgen.placement;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.magicteam.relicsandfoes.init.RAFDimensions;

import java.util.stream.Stream;

public class ExcludeRegionsPlacement extends PlacementModifier {
    public static final ExcludeRegionsPlacement INSTANCE = new ExcludeRegionsPlacement();
    public static final MapCodec<ExcludeRegionsPlacement> CODEC = MapCodec.unit(INSTANCE);

    private ExcludeRegionsPlacement() {}

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        int middleX = (pos.getX() + 1024) >> 11 << 11;
        int middleZ = (pos.getZ() + 1024) >> 11 << 11;
        if (Mth.lengthSquared(middleX - pos.getX(), middleZ - pos.getZ()) <= 160 * 160) { // 通界古墟
            return Stream.empty();
        }
        return Stream.of(pos);
    }

    @Override
    public PlacementModifierType<?> type() {
        return RAFDimensions.EXCLUDE_REGIONS_PLACEMENT.get();
    }
}
