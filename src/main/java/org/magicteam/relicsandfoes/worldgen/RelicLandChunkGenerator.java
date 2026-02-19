package org.magicteam.relicsandfoes.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class RelicLandChunkGenerator extends NoiseBasedChunkGenerator {
    public RelicLandChunkGenerator(RelicLandBiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource, settings);
    }
}
