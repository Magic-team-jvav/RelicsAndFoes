package org.magicteam.relicsandfoes.mixed;

import net.minecraft.world.level.levelgen.NoiseChunk;

public interface RAFNoiseChunk {
    void raf$setFiller(NoiseChunk.BlockStateFiller filler);

    static RAFNoiseChunk of(NoiseChunk chunk) {
        return (RAFNoiseChunk) chunk;
    }
}
