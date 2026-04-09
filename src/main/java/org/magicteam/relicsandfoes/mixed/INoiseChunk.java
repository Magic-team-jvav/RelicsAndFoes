package org.magicteam.relicsandfoes.mixed;

import net.minecraft.world.level.levelgen.NoiseChunk;

public interface INoiseChunk {
    void raf$mergeFiller(NoiseChunk.BlockStateFiller filler);

    boolean raf$merged();

    void raf$setMerged(boolean merged);

    static INoiseChunk of(NoiseChunk chunk) {
        return (INoiseChunk) chunk;
    }
}
