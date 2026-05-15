package org.magicteam.relicsandfoes.mixin.net.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.FogRenderer;
import org.magicteam.relicsandfoes.client.RAFSharedValues;
import org.magicteam.relicsandfoes.client.RelicsAndFoesClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @ModifyArg(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 1), index = 1)
    private static float modifyDarken(float value) {
        return RelicsAndFoesClient.timeOfDayLow(value);
    }

    @ModifyExpressionValue(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel$ClientLevelData;getClearColorScale()F"))
    private static float modifyScale(float original) {
        return RAFSharedValues.inRelicLand ? 1 : original;
    }
}
