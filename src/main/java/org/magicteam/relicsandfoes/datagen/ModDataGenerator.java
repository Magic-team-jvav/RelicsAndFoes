package org.magicteam.relicsandfoes.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.ModDimensions;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = RelicsAndFoes.MODID)
public final class ModDataGenerator {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, ModDimensions.DimensionTypez::bootstrap)
            .add(Registries.LEVEL_STEM, ModDimensions.LevelStems::bootstrap)
            .add(Registries.NOISE_SETTINGS, ModDimensions.NoiseGeneratorSettingz::bootstrap)
            .add(Registries.BIOME, ModDimensions.Biomez::bootstrap)
            .add(Registries.NOISE, ModDimensions.Noisez::bootstrap);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        DatapackBuiltinEntriesProvider provider = generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, lookup, BUILDER, Collections.singleton(RelicsAndFoes.MODID)));
    }
}
