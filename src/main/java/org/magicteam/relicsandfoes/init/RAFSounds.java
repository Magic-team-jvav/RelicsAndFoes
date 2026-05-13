package org.magicteam.relicsandfoes.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;

public final class RAFSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, RelicsAndFoes.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> RUIN_CITY_BEFORE_BOSS_PROTOTYPE = SOUND_EVENTS.register("ruin_city_before_boss_prototype", SoundEvent::createVariableRangeEvent);
    public static final DeferredHolder<SoundEvent, SoundEvent> THE_SUNKEN_EXPANSE = SOUND_EVENTS.register("the_sunken_expanse", SoundEvent::createVariableRangeEvent);
}
