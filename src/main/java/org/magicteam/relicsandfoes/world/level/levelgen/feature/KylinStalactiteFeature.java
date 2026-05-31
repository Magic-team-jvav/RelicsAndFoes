package org.magicteam.relicsandfoes.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.magicteam.relicsandfoes.init.RAFBlocks;
import org.magicteam.relicsandfoes.world.block.KylinStalactiteBlock;

public class KylinStalactiteFeature extends Feature<KylinStalactiteFeature.Config> {
    public KylinStalactiteFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int[] height = new int[9];
        mutable.setY(origin.getY());
        for (int i = 0; i < 3; i++) {
            mutable.setX(origin.getX() + i - 1);
            for (int j = 0; j < 3; j++) {
                if (!level.isEmptyBlock(mutable.setZ(origin.getZ() + j - 1))) {
                    return false;
                }
                BlockPos below = mutable.below();
                if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
                    return false;
                }
                height[i * 3 + j] = i == 1 && j == 1 ? 2 : (random.nextInt(5) == 0 ? 1 : 0);
            }
        }

        BlockState state = RAFBlocks.KYLIN_STALACTITE.get().defaultBlockState();
        BlockState tip = state.setValue(KylinStalactiteBlock.IS_TIP, true);
        BlockState base = state.setValue(KylinStalactiteBlock.IS_TIP, false);
        for (int i = 0; i < 3; i++) {
            mutable.setX(origin.getX() + i - 1);
            for (int j = 0; j < 3; j++) {
                int h = height[i * 3 + j];
                if (h == 0) continue;
                mutable.setZ(origin.getZ() + j - 1);
                for (int k = 0; k < h; k++) {
                    level.setBlock(mutable.setY(origin.getY() + k), k == h - 1 ? tip : base, Block.UPDATE_CLIENTS);
                }
            }
        }

        return true;
    }

    public static final class Config implements FeatureConfiguration {
        public static final Config INSTANCE = new Config();
        public static final Codec<Config> CODEC = Codec.unit(INSTANCE);

        private Config() {}
    }
}
