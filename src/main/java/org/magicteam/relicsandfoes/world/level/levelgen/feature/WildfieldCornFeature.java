package org.magicteam.relicsandfoes.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.magicteam.relicsandfoes.init.RAFBlocks;

public class WildfieldCornFeature extends Feature<WildfieldCornFeature.Config> {
    public WildfieldCornFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        BlockPos.MutableBlockPos origin = context.origin().mutable();
        WorldGenLevel level = context.level();
        if (level.isEmptyBlock(origin.setY(60))) {
            Config config = context.config();
            for (int y = origin.getY(); y > 40; y--) {
                BlockState state = level.getBlockState(origin.setY(y));
                if (state.canBeReplaced()) continue;
                if (RAFBlocks.WILDFIELD_CORN_STALK.get().canSurvive(state, level, origin)) {
                    RandomSource random = context.random();
                    int height = random.nextIntBetweenInclusive(config.minHeight, config.maxHeight);
                    for (int h = 0; h < height; h++) {
                        level.setBlock(origin.setY(y + h + 1), RAFBlocks.WILDFIELD_CORN_STALK.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                    level.setBlock(origin.setY(y + height + 1), RAFBlocks.WILDFIELD_CORN.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                    return true;
                }
            }
        }
        return false;
    }

    public record Config(int minHeight, int maxHeight) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("min_height").forGetter(Config::minHeight),
                ExtraCodecs.POSITIVE_INT.fieldOf("max_height").forGetter(Config::maxHeight)
        ).apply(instance, Config::new));
    }
}
