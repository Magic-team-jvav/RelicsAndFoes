package org.magicteam.relicsandfoes.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BushBlock;

public class ThicketBlock extends BushBlock {
    public static final MapCodec<ThicketBlock> CODEC = simpleCodec(ThicketBlock::new);

    public ThicketBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<ThicketBlock> codec() {
        return CODEC;
    }
}
