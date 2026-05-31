package org.magicteam.relicsandfoes.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.magicteam.relicsandfoes.world.block.StalkBlockHead;
import org.magicteam.relicsandfoes.world.block.StalkBlockStem;

public class StalkBlockFeature extends Feature<StalkBlockFeature.Config> {
    public StalkBlockFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        BlockPos.MutableBlockPos origin = context.origin().mutable();
        WorldGenLevel level = context.level();
        Config config = context.config();
        int oy = origin.getY();

        BlockState stemState = config.stem.defaultBlockState();
        if (!config.stem.canSurvive(stemState, level, origin)) {
            return false;
        }

        int height = context.random().nextIntBetweenInclusive(config.minHeight, config.maxHeight);
        for (int y = oy + height; y >= oy; y--) {
            if (!level.isEmptyBlock(origin.setY(y))) {
                return false;
            }
        }

        for (int h = 0; h < height; h++) {
            level.setBlock(origin.setY(oy + h), stemState, Block.UPDATE_CLIENTS);
        }
        level.setBlock(origin.setY(oy + height), config.head.defaultBlockState(), Block.UPDATE_CLIENTS);
        return true;
    }

    public record Config(int minHeight, int maxHeight, StalkBlockHead head, StalkBlockStem stem) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("min_height").forGetter(Config::minHeight),
                ExtraCodecs.POSITIVE_INT.fieldOf("max_height").forGetter(Config::maxHeight),
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("head").forGetter(Config::head),
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("stem").forGetter(Config::stem)
        ).apply(instance, (i1, i2, b1, b2) -> new Config(i1, i2, (StalkBlockHead) b1, (StalkBlockStem) b2)));
    }
}
