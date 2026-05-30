package org.magicteam.relicsandfoes.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.common.util.TriState;
import org.magicteam.relicsandfoes.init.RAFDimensions;
import org.magicteam.relicsandfoes.world.level.levelgen.RelicLandChunkGenerator;

import java.util.Optional;

public class InThePeachBlossomValeFilter extends PlacementFilter {
    public static final MapCodec<InThePeachBlossomValeFilter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("on_center").forGetter(f -> getter(f.onCenter)),
            Codec.BOOL.optionalFieldOf("on_floor").forGetter(f -> getter(f.onFloor))
    ).apply(instance, (onCenter, onFloor) -> new InThePeachBlossomValeFilter(setter(onCenter), setter(onFloor))));

    private static Optional<Boolean> getter(TriState state) {
        return state.isDefault() ? Optional.empty() : (state.isTrue() ? Optional.of(Boolean.TRUE) : Optional.of(Boolean.FALSE));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static TriState setter(Optional<Boolean> optional) {
        return optional.map(bool -> bool ? TriState.TRUE : TriState.FALSE).orElse(TriState.DEFAULT);
    }

    private final TriState onCenter;
    private final TriState onFloor;

    private InThePeachBlossomValeFilter(TriState onCenter, TriState onFloor) {
        this.onCenter = onCenter;
        this.onFloor = onFloor;
    }

    public static InThePeachBlossomValeFilter of(TriState onCenter, TriState onFloor) {
        return new InThePeachBlossomValeFilter(onCenter, onFloor);
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        int worldX = pos.getX();
        int worldZ = pos.getZ();
        int biomeX = (worldX >> 11 << 11) + 1024;
        int biomeZ = (worldZ >> 11 << 11) + 1024;
        if (!onCenter.isDefault()) {
            if (onCenter.isTrue() != Mth.lengthSquared(biomeX - worldX, biomeZ - worldZ) < 145 * 145) {
                return false;
            }
        }
        if (!onFloor.isDefault()) {
            if (onFloor.isTrue() != ((RelicLandChunkGenerator) context.generator()).isPeachValeValleyFloor(worldX, worldZ, biomeX, biomeZ)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public PlacementModifierType<?> type() {
        return RAFDimensions.IN_THE_PEACH_BLOSSOM_VALE_FILTER.get();
    }
}
