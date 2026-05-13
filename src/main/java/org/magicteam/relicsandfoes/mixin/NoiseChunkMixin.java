package org.magicteam.relicsandfoes.mixin;

import net.minecraft.world.level.levelgen.NoiseChunk;
import org.magicteam.relicsandfoes.mixed.INoiseChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NoiseChunk.class)
public abstract class NoiseChunkMixin implements INoiseChunk {
    @Mutable
    @Shadow
    @Final
    private NoiseChunk.BlockStateFiller blockStateRule;

    @Override
    public void raf$setFiller(NoiseChunk.BlockStateFiller filler) {
        this.blockStateRule = filler;
    }
}
