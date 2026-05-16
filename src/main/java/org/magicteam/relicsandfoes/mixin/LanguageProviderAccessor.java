package org.magicteam.relicsandfoes.mixin;

import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@org.spongepowered.asm.mixin.Mixin(net.neoforged.neoforge.common.data.LanguageProvider.class)
public interface LanguageProviderAccessor {
    @Accessor
    Map<String, String> getData();
}
