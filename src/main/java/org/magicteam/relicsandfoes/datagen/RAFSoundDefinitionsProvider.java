package org.magicteam.relicsandfoes.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.RAFSounds;

public class RAFSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public RAFSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, RelicsAndFoes.MODID, helper);
    }

    @Override
    public void registerSounds() {
        biomeMusic(RAFSounds.RUIN_CITY_BEFORE_BOSS_PROTOTYPE);
        biomeMusic(RAFSounds.THE_SUNKEN_EXPANSE);
    }

    protected void biomeMusic(DeferredHolder<SoundEvent, SoundEvent> holder) {
        add(holder, SoundDefinition.definition().with(SoundDefinition.Sound.sound(holder.getId(), SoundDefinition.SoundType.SOUND).stream().volume(0.4)));
    }
}
