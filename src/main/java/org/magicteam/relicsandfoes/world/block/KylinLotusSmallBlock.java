package org.magicteam.relicsandfoes.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public class KylinLotusSmallBlock extends FloatingKylinLotus {
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getFluidState(pos.below()).is(FluidTags.WATER);
    }
}
