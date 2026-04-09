package org.magicteam.relicsandfoes.mixin;

import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.material.MaterialRuleList;
import org.magicteam.relicsandfoes.mixed.INoiseChunk;
import org.spongepowered.asm.mixin.*;

import java.util.List;

@Mixin(NoiseChunk.class)
public abstract class NoiseChunkMixin implements INoiseChunk {
    @Mutable
    @Shadow
    @Final
    private NoiseChunk.BlockStateFiller blockStateRule;

    @Unique
    private boolean raf$merged = false;

    @Override
    public void raf$mergeFiller(NoiseChunk.BlockStateFiller filler) {
        if (raf$merged) return;
        this.blockStateRule = new MaterialRuleList(List.of(filler, blockStateRule));
        this.raf$merged = true;
    }

    @Override
    public boolean raf$merged() {
        return raf$merged;
    }

    @Override
    public void raf$setMerged(boolean merged) {
        this.raf$merged = merged;
    }
}
