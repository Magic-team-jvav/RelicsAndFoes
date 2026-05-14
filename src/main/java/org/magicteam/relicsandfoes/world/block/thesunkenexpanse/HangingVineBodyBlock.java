package org.magicteam.relicsandfoes.world.block.thesunkenexpanse;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.phys.shapes.Shapes;
import org.magicteam.relicsandfoes.init.RAFBlocks;

public class HangingVineBodyBlock extends GrowingPlantBodyBlock {
    public static final MapCodec<HangingVineBodyBlock> CODEC = simpleCodec(HangingVineBodyBlock::new);

    public HangingVineBodyBlock(Properties properties) {
        super(properties, Direction.DOWN, Shapes.block(), false);
    }

    @Override
    protected MapCodec<HangingVineBodyBlock> codec() {
        return CODEC;
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return RAFBlocks.HANGING_VINE_HEAD.get();
    }
}
