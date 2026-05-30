package org.magicteam.relicsandfoes.world.level.levelgen.placement;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.magicteam.relicsandfoes.init.RAFDimensions;

/// 通界古墟
public class NotInCrossRealmAncientRuinsFilter extends PlacementFilter {
    public static final NotInCrossRealmAncientRuinsFilter INSTANCE = new NotInCrossRealmAncientRuinsFilter();
    public static final MapCodec<NotInCrossRealmAncientRuinsFilter> CODEC = MapCodec.unit(INSTANCE);

    private NotInCrossRealmAncientRuinsFilter() {}

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        int worldX = pos.getX();
        int worldZ = pos.getZ();
        return Mth.lengthSquared(((worldX + 1024) >> 11 << 11) - worldX, ((worldZ + 1024) >> 11 << 11) - worldZ) > 160 * 160;
    }

    @Override
    public PlacementModifierType<?> type() {
        return RAFDimensions.NOT_IN_CROSS_REALM_ANCIENT_RUINS_FILTER.get();
    }
}
