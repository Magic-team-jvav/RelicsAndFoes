package org.magicteam.relicsandfoes.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.ObjModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.RAFBlocks;

public class RAFBlockStateProvider extends BlockStateProvider {
    public static final ModelFile ITEM_GENERATED = new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace("item/generated"));
    public static final ModelFile BLOCK_TINTED_CROSS = new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace("block/tinted_cross"));
    public static final ModelFile BLOCK_CROP = new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace("block/crop"));

    public RAFBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, RelicsAndFoes.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        obj(RAFBlocks.WILDFIELD_CORN);
        obj(RAFBlocks.WILDFIELD_CORN_STALK);
        obj(RAFBlocks.TEQUILA);
        obj(RAFBlocks.WILDFIELD_WEEDS);
        obj(RAFBlocks.ABANDONED_PLANKS, false);
        obj(RAFBlocks.ABANDONED_SIGN);
        ancientWildgrass();
        fallenLavender();
        ancientTallWildgrass();
        abandonedTabled();
    }

    private void abandonedTabled() {
        ResourceLocation id = RAFBlocks.ABANDONED_TABLET.getId();
        ModelFile model = new ModelFile.UncheckedModelFile(id.withPrefix("block/"));
        getVariantBuilder(RAFBlocks.ABANDONED_TABLET.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(model)
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                .build());
        itemModels().getBuilder(id.getPath()).parent(model);
    }

    private void ancientTallWildgrass() {
        String path = RAFBlocks.ANCIENT_TALL_WILDGRASS.getId().getPath();
        ResourceLocation name = RelicsAndFoes.asResource("block/" + path);
        ResourceLocation bottom = name.withSuffix("_bottom");
        ResourceLocation top = name.withSuffix("_top");
        getVariantBuilder(RAFBlocks.ANCIENT_TALL_WILDGRASS.get()).forAllStates(state -> {
            if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                return new ConfiguredModel[]{new ConfiguredModel(new ModelFile.UncheckedModelFile(bottom))};
            }
            return new ConfiguredModel[]{new ConfiguredModel(new ModelFile.UncheckedModelFile(top))};
        });
        models().getBuilder(name.getPath() + "_bottom").parent(BLOCK_TINTED_CROSS).texture("cross", bottom).renderType("cutout");
        models().getBuilder(name.getPath() + "_top").parent(BLOCK_TINTED_CROSS).texture("cross", top).renderType("cutout");
        itemModels().getBuilder(path).parent(ITEM_GENERATED).texture("layer0", RelicsAndFoes.asResource("item/" + path));
    }

    private void fallenLavender() {
        String path = RAFBlocks.FALLEN_LAVENDER.getId().getPath();
        ResourceLocation name = RelicsAndFoes.asResource("block/" + path);
        getVariantBuilder(RAFBlocks.FALLEN_LAVENDER.get()).partialState()
                .addModels(new ConfiguredModel(new ModelFile.UncheckedModelFile(name)));
        models().getBuilder(name.getPath()).parent(BLOCK_CROP).texture("crop", name).renderType("cutout");
        itemModels().getBuilder(path).parent(ITEM_GENERATED).texture("layer0", name);
    }

    private void ancientWildgrass() {
        String path = RAFBlocks.ANCIENT_WILDGRASS.getId().getPath();
        ResourceLocation name = RelicsAndFoes.asResource("block/" + path);
        ResourceLocation name0 = name.withSuffix("0");
        ResourceLocation name1 = name.withSuffix("1");
        ResourceLocation name2 = name.withSuffix("2");
        getVariantBuilder(RAFBlocks.ANCIENT_WILDGRASS.get()).partialState().addModels(
                new ConfiguredModel(new ModelFile.UncheckedModelFile(name0)),
                new ConfiguredModel(new ModelFile.UncheckedModelFile(name1)),
                new ConfiguredModel(new ModelFile.UncheckedModelFile(name2))
        );
        models().getBuilder(name0.getPath()).parent(BLOCK_TINTED_CROSS).texture("cross", name0).renderType("cutout");
        models().getBuilder(name1.getPath()).parent(BLOCK_TINTED_CROSS).texture("cross", name1).renderType("cutout");
        models().getBuilder(name2.getPath()).parent(BLOCK_TINTED_CROSS).texture("cross", name2).renderType("cutout");
        itemModels().getBuilder(path).parent(ITEM_GENERATED).texture("layer0", RelicsAndFoes.asResource("item/" + path));
    }

    protected void obj(DeferredBlock<?> block) {
        obj(block, true);
    }

    protected void obj(DeferredBlock<?> block, boolean cutout) {
        String path = block.getId().getPath();
        ResourceLocation name = RelicsAndFoes.asResource("block/" + path);
        BlockModelBuilder builder = models().withExistingParent(name.getPath(), ResourceLocation.withDefaultNamespace("block/block")).texture("particle", name);
        if (cutout) {
            builder.renderType("cutout");
        }
        builder.customLoader(ObjModelBuilder::begin)
                .automaticCulling(false)
                .flipV(true)
                .modelLocation(RelicsAndFoes.asResource("models/" + path + ".obj"))
                .overrideMaterialLibrary(RelicsAndFoes.asResource("models/" + path + ".mtl"));
        simpleBlockWithItem(block.get(), new ModelFile.UncheckedModelFile(name));
    }
}
