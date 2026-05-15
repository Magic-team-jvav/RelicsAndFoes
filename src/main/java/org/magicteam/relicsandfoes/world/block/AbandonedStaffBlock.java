package org.magicteam.relicsandfoes.world.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AbandonedStaffBlock extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final MapCodec<AbandonedStaffBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            propertiesCodec(),
            BoundingBox.CODEC.fieldOf("shape").forGetter(block -> {
                AABB bounds = block.shape.bounds();
                return new BoundingBox(
                        (int) (bounds.minX * 16),
                        (int) (bounds.minY * 16),
                        (int) (bounds.minZ * 16),
                        (int) (bounds.maxX * 16),
                        (int) (bounds.maxY * 16),
                        (int) (bounds.maxZ * 16)
                );
            })
    ).apply(instance, (properties, boundingBox) -> new AbandonedStaffBlock(properties, box(
            boundingBox.minX(),
            boundingBox.minY(),
            boundingBox.minZ(),
            boundingBox.maxX(),
            boundingBox.maxY(),
            boundingBox.maxZ()
    ))));

    private final VoxelShape shape;

    public AbandonedStaffBlock(Properties properties, VoxelShape shape) {
        super(properties);
        this.shape = shape;
    }

    @Override
    protected MapCodec<AbandonedStaffBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
