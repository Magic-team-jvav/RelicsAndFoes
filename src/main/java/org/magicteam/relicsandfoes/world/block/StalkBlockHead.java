package org.magicteam.relicsandfoes.world.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class StalkBlockHead extends BushBlock {
    public static final MapCodec<StalkBlockHead> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            propertiesCodec(),
            Block.CODEC.fieldOf("stem").forGetter(b -> b.stem.get())
    ).apply(instance, (p, b) -> new StalkBlockHead(p, () -> (StalkBlockStem) b)));
    static final VoxelShape SHAPE = box(2, 0, 2, 14, 16, 14);

    private final Supplier<? extends StalkBlockStem> stem;

    public StalkBlockHead(Properties properties, Supplier<? extends StalkBlockStem> stem) {
        super(properties);
        this.stem = stem;
    }

    @Override
    protected MapCodec<StalkBlockHead> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(stem.get()) || super.mayPlaceOn(state, level, pos);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        return mayPlaceOn(level.getBlockState(belowPos), level, belowPos);
    }
}
