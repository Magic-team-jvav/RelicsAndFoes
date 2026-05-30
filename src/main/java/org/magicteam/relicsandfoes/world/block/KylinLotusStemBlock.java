package org.magicteam.relicsandfoes.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class KylinLotusStemBlock extends StalkBlockStem {
    public KylinLotusStemBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.TORCHFLOWER).mapColor(MapColor.COLOR_LIGHT_BLUE));
        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.ATTACHED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.ATTACHED);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.getBlockState(pos.below()).is(this)) {
            level.setBlock(pos, state.setValue(BlockStateProperties.ATTACHED, true), Block.UPDATE_CLIENTS);
        }
    }
}
