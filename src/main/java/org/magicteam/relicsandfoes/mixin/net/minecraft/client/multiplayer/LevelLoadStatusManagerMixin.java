package org.magicteam.relicsandfoes.mixin.net.minecraft.client.multiplayer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.LevelLoadStatusManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.magicteam.relicsandfoes.init.RAFDimensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelLoadStatusManager.class)
public abstract class LevelLoadStatusManagerMixin {
    @Shadow
    @Final
    private ClientLevel level;

    @Shadow
    @Final
    private LocalPlayer player;

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;isOutsideBuildHeight(I)Z"))
    private boolean checkIfChunkLoaded(boolean original) {
        if (original && level.dimension() == RAFDimensions.LEVEL) {
            player.setPos(0, 64, 0);
            LevelChunk chunk = level.getChunkSource().getChunk(0, 0, ChunkStatus.FULL, false);
            if (chunk == null || chunk.isEmpty()) {
                return false;
            }
        }
        return original;
    }
}
