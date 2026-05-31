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

public class WorldSurfaceOffsetPlacement extends PlacementModifier {
    public static final MapCodec<WorldSurfaceOffsetPlacement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.lenientOptionalFieldOf("x_offset", 0).forGetter(p -> p.xOffset),
            Codec.INT.fieldOf("y_offset").forGetter(p -> p.yOffset),
            Codec.INT.lenientOptionalFieldOf("z_offset", 0).forGetter(p -> p.zOffset),
            Codec.BOOL.lenientOptionalFieldOf("offset_final_xz", false).forGetter(p -> p.offsetFinalXZ)
    ).apply(instance, WorldSurfaceOffsetPlacement::new));

    private final int xOffset;
    private final int yOffset;
    private final int zOffset;
    private final boolean offsetFinalXZ;

    private WorldSurfaceOffsetPlacement(int xOffset, int yOffset, int zOffset, boolean offsetFinalXZ) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.offsetFinalXZ = offsetFinalXZ;
    }

    public static WorldSurfaceOffsetPlacement of(int xOffset, int yOffset, int zOffset, boolean offsetFinalXZ) {
        return new WorldSurfaceOffsetPlacement(xOffset, yOffset, zOffset, offsetFinalXZ);
    }

    public static WorldSurfaceOffsetPlacement of(int xOffset, int yOffset, int zOffset) {
        return of(xOffset, yOffset, zOffset, false);
    }

    public static WorldSurfaceOffsetPlacement of(int yOffset) {
        return of(0, yOffset, 0);
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        int worldX = pos.getX() + xOffset;
        int worldZ = pos.getZ() + zOffset;
        int height = context.getHeight(Heightmap.Types.WORLD_SURFACE_WG, worldX, worldZ);
        int finalX = offsetFinalXZ ? worldX : pos.getX();
        int finalZ = offsetFinalXZ ? worldZ : pos.getZ();
        return height > context.getMinBuildHeight() ? Stream.of(new BlockPos(finalX, height + yOffset, finalZ)) : Stream.of();
    }

    @Override
    public PlacementModifierType<?> type() {
        return RAFDimensions.WORLD_SURFACE_WITH_Y_OFFSET_PLACEMENT.get();
    }
}
