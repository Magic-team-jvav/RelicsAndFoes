package org.magicteam.relicsandfoes.mixin.net.minecraft.client.multiplayer;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.multiplayer.ClientLevel;
import org.magicteam.relicsandfoes.client.RAFSharedValues;
import org.magicteam.relicsandfoes.client.RelicsAndFoesClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    @ModifyArg(method = "getSkyColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F"), index = 1)
    private float modifyDarken(float value) {
        return RelicsAndFoesClient.timeOfDayLow(value);
    }

    @Mixin(ClientLevel.ClientLevelData.class)
    public static abstract class ClientLevelDataMixin {
        @ModifyReturnValue(method = "getHorizonHeight", at = @At("RETURN"))
        private double modifyHeight(double original) {
            return RAFSharedValues.inRelicLand ? 0 : original;
        }
    }
}
