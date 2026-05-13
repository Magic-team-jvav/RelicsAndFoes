package org.magicteam.relicsandfoes.mixed;

import net.minecraft.world.level.levelgen.NoiseChunk;

public interface INoiseChunk {
    void raf$setFiller(NoiseChunk.BlockStateFiller filler);

    static INoiseChunk of(NoiseChunk chunk) {
        return (INoiseChunk) chunk;
    }
}
