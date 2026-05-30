package org.magicteam.relicsandfoes.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.magicteam.relicsandfoes.init.RAFDimensions;

import java.util.stream.Stream;

public class WorldSurfaceWithYOffsetPlacement extends PlacementModifier {
    public static final MapCodec<WorldSurfaceWithYOffsetPlacement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("y_offset").forGetter(p -> p.offsetY)
    ).apply(instance, WorldSurfaceWithYOffsetPlacement::new));

    private final int offsetY;

    private WorldSurfaceWithYOffsetPlacement(int offsetY) {
        this.offsetY = offsetY;
    }

    public static WorldSurfaceWithYOffsetPlacement of(int offsetY) {
        return new WorldSurfaceWithYOffsetPlacement(offsetY);
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        int worldX = pos.getX();
        int worldZ = pos.getZ();
        int height = context.getHeight(Heightmap.Types.WORLD_SURFACE_WG, worldX, worldZ);
        return height > context.getMinBuildHeight() ? Stream.of(new BlockPos(worldX, height + offsetY, worldZ)) : Stream.of();
    }

    @Override
    public PlacementModifierType<?> type() {
        return RAFDimensions.WORLD_SURFACE_WITH_Y_OFFSET_PLACEMENT.get();
    }
}
