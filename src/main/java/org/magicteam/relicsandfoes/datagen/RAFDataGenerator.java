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
import org.magicteam.relicsandfoes.init.RAFDimensions;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = RelicsAndFoes.MODID)
public final class RAFDataGenerator {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, RAFDimensions.DimensionTypez::bootstrap)
            .add(Registries.LEVEL_STEM, RAFDimensions.LevelStemz::bootstrap)
            .add(Registries.NOISE_SETTINGS, RAFDimensions.NoiseGeneratorSettingz::bootstrap)
            .add(Registries.BIOME, RAFDimensions.Biomez::bootstrap)
            .add(Registries.STRUCTURE, RAFDimensions.Structurez::bootstrap)
            .add(Registries.STRUCTURE_SET, RAFDimensions.StructureSetz::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, RAFDimensions.ConfiguredFeaturez::bootstrap)
            .add(Registries.PLACED_FEATURE, RAFDimensions.PlacedFeaturez::bootstrap);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        boolean client = event.includeClient();
        generator.addProvider(client, new RAFBlockStateProvider(output, helper));
        generator.addProvider(client, new RAFItemModelProvider(output, helper));
//        generator.addProvider(client, new RAFSoundDefinitionsProvider(output));
        generator.addProvider(client, new RAFLanguageProvider(output, true, "en_us"));
        generator.addProvider(client, new RAFLanguageProvider(output, false, "zh_cn"));
        generator.addProvider(client, new RAFEnUdProvider(output));

        DatapackBuiltinEntriesProvider provider = generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, lookup, BUILDER, Collections.singleton(RelicsAndFoes.MODID)));
    }
}
