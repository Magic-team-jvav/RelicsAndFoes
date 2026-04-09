package org.magicteam.relicsandfoes.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.magicteam.relicsandfoes.init.ModDimensions;
import org.magicteam.relicsandfoes.mixed.INoiseChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin {
    @Unique
    private NormalNoise raf$noise;

    @ModifyExpressionValue(method = "doFill", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkAccess;getOrCreateNoiseChunk(Ljava/util/function/Function;)Lnet/minecraft/world/level/levelgen/NoiseChunk;"))
    private NoiseChunk tryMergeFiller(NoiseChunk original, @Local(argsOnly = true) ChunkAccess chunk) {
        INoiseChunk i = INoiseChunk.of(original);
        if (i.raf$merged()) return original;
        if (chunk.levelHeightAccessor instanceof ServerLevel level) {
            if (level.dimension() == ModDimensions.LEVEL) {
                if (raf$noise == null) {
                    RandomSource random = RandomSource.create(level.getSeed());
                    double[] amplitudes = new double[random.nextInt(3, 6)];
                    for (int j = 0; j < amplitudes.length; j++) {
                        amplitudes[j] = random.nextDouble();
                    }
                    this.raf$noise = NormalNoise.create(random, -9, amplitudes);
                }
                i.raf$mergeFiller(ModDimensions.filler(raf$noise));
            } else {
                i.raf$setMerged(true);
            }
        }
        return original;
    }
}
