package org.magicteam.relicsandfoes.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.RAFBlocks;

public class RAFItemModelProvider extends ItemModelProvider {
    public RAFItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, RelicsAndFoes.MODID, helper);
    }

    @Override
    protected void registerModels() {
        builtinEntity(RAFBlocks.ANCIENT_PORTAL.asItem());
    }

    private ItemModelBuilder builtinEntity(Item item) {
        return builtinEntity(BuiltInRegistries.ITEM.getKey(item));
    }

    private ItemModelBuilder builtinEntity(ResourceLocation item) {
        return getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("builtin/entity"));
    }
}
