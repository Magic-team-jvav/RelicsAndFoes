package org.magicteam.relicsandfoes.mixin.net.minecraft.client.multiplayer;

import net.minecraft.client.multiplayer.ClientLevel;
import org.magicteam.relicsandfoes.client.RAFClientEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    @ModifyArg(method = "getSkyColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F"), index = 1)
    private float modifyDarken(float value) {
        return RAFClientEvents.timeOfDayLow(value);
    }
}
