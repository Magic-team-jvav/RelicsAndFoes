package org.magicteam.relicsandfoes.world.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.function.Supplier;

public class HangingVineHeadBlock extends GrowingPlantHeadBlock {
    public static final MapCodec<HangingVineHeadBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            propertiesCodec(),
            Block.CODEC.fieldOf("body").forGetter(HangingVineHeadBlock::getBodyBlock)
    ).apply(instance, (p, b) -> new HangingVineHeadBlock(p, () -> (HangingVineBodyBlock) b)));

    private final Supplier<? extends HangingVineBodyBlock> body;

    public HangingVineHeadBlock(Properties properties, Supplier<? extends HangingVineBodyBlock> body) {
        super(properties, Direction.DOWN, Shapes.block(), false, 0.1);
        this.body = body;
    }

    @Override
    protected MapCodec<HangingVineHeadBlock> codec() {
        return CODEC;
    }

    @Override
    protected Block getBodyBlock() {
        return body.get();
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
