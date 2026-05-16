package org.magicteam.relicsandfoes.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.util.BedrockSoundConvertor;

import java.util.List;
import java.util.Set;

public class RAFSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public RAFSoundDefinitionsProvider(PackOutput output) {
        super(output, RelicsAndFoes.MODID, new ExistingFileHelper(List.of(), Set.of(), false, null, null));
    }

    @Override
    public void registerSounds() {
        BedrockSoundConvertor.convert().forEach(this::add);
    }
}
