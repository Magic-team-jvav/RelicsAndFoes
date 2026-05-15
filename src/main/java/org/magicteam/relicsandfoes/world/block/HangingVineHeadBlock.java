package org.magicteam.relicsandfoes.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import org.magicteam.relicsandfoes.init.RAFBlocks;

public class HangingVineHeadBlock extends GrowingPlantHeadBlock {
    public static final MapCodec<HangingVineHeadBlock> CODEC = simpleCodec(HangingVineHeadBlock::new);

    public HangingVineHeadBlock(Properties properties) {
        super(properties, Direction.DOWN, Shapes.block(), false, 0.1);
    }

    @Override
    protected MapCodec<HangingVineHeadBlock> codec() {
        return CODEC;
    }

    @Override
    protected Block getBodyBlock() {
        return RAFBlocks.HANGING_VINE_BODY.get();
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1;
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.isAir();
    }
}
