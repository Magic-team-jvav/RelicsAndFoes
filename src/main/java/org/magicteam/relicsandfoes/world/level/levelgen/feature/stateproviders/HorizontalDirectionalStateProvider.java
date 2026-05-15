package org.magicteam.relicsandfoes.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import org.magicteam.relicsandfoes.init.RAFDimensions;

public class HorizontalDirectionalStateProvider extends BlockStateProvider {
    public static final MapCodec<HorizontalDirectionalStateProvider> CODEC = BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block")
            .xmap(HorizontalDirectionalStateProvider::new, HorizontalDirectionalStateProvider::getBlock);
    private static final Direction[] DIRECTIONS = HorizontalDirectionalBlock.FACING.getPossibleValues().toArray(Direction[]::new);

    private final Block block;

    public HorizontalDirectionalStateProvider(Block block) {
        this.block = block;
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return RAFDimensions.HORIZONTAL_DIRECTIONAL_STATE_PROVIDER.get();
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public BlockState getState(RandomSource random, BlockPos pos) {
        return block.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Util.getRandom(DIRECTIONS, random));
    }
}
