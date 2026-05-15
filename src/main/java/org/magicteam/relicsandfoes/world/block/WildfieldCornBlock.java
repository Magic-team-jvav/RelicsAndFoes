package org.magicteam.relicsandfoes.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.magicteam.relicsandfoes.init.RAFBlocks;

public class WildfieldCornBlock extends BushBlock {
    public static final MapCodec<WildfieldCornBlock> CODEC = simpleCodec(WildfieldCornBlock::new);
    public static final VoxelShape SHAPE = box(2, 0, 2, 14, 16, 14);

    public WildfieldCornBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<WildfieldCornBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(RAFBlocks.WILDFIELD_CORN_STALK);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        return mayPlaceOn(level.getBlockState(belowPos), level, belowPos);
    }
}
