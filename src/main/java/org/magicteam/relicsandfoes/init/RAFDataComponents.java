package org.magicteam.relicsandfoes.init;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.component.LostSoloMelodyComponent;

public final class RAFDataComponents {
    public static final DeferredRegister.DataComponents TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RelicsAndFoes.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LostSoloMelodyComponent>> LOST_SOLO_MELODY = TYPES.registerComponentType("lost_solo_melody", builder -> builder.persistent(LostSoloMelodyComponent.CODEC).networkSynchronized(LostSoloMelodyComponent.STREAM_CODEC));
}
