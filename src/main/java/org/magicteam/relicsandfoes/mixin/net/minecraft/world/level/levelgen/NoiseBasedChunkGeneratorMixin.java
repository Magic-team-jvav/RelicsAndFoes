package org.magicteam.relicsandfoes.mixin.net.minecraft.world.level.levelgen;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import org.magicteam.relicsandfoes.world.level.levelgen.RelicLandChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin {
    @WrapWithCondition(method = "iterateNoiseColumn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseChunk;initializeForFirstCellX()V"))
    private boolean setup(NoiseChunk instance) {
        if (((Object) this) instanceof RelicLandChunkGenerator generator) {
            generator.setupFiller(instance);
        }
        return true;
    }
}
