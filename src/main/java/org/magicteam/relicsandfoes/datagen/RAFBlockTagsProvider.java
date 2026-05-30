package org.magicteam.relicsandfoes.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.RAFBlocks;
import org.magicteam.relicsandfoes.init.RAFTags;

import java.util.concurrent.CompletableFuture;

public class RAFBlockTagsProvider extends BlockTagsProvider {
    public RAFBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, RelicsAndFoes.MODID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(RAFTags.Blocks.FALLEN_CITY_REMNANT_GEAR).add(
                RAFBlocks.ANCIENT_RELIC_TELEPORTER.get(),
                RAFBlocks.ANCIENT_RELIC_RESPAWN_PLATFORM.get(),
                RAFBlocks.WILDFIELD_CORN.get(),
                RAFBlocks.WILDFIELD_CORN_STALK.get(),
                RAFBlocks.WILDFIELD_WEEDS.get(),
                RAFBlocks.FALLEN_LAVENDER.get(),
                RAFBlocks.FALLEN_COFFER.get(),
                RAFBlocks.TEQUILA.get(),
                RAFBlocks.HANGING_VINE_HEAD.get(),
                RAFBlocks.HANGING_VINE_BODY.get(),
                RAFBlocks.PROTOTYPE_BOX.get()
        );
        tag(RAFTags.Blocks.HILLSIDE_XIE_BEAST).add(
                RAFBlocks.KYLIN_BAMBOO_TOP.get(),
                RAFBlocks.KYLIN_BAMBOO_STEM.get(),
                RAFBlocks.KYLIN_BAMBOO_GREEN_TOP.get(),
                RAFBlocks.KYLIN_BAMBOO_GREEN_STEM.get(),
                RAFBlocks.KYLIN_LOTUS.get(),
                RAFBlocks.KYLIN_LOTUS_STEM.get(),
                RAFBlocks.FLOATING_KYLIN_LOTUS.get(),
                RAFBlocks.KYLIN_LOTUS_SMALL.get(),
                RAFBlocks.KYLIN_STALACTITE.get(),
                RAFBlocks.KILIN_BOX.get()
        );


        tag(BlockTags.CLIMBABLE).add(
                RAFBlocks.FOREST_VINE.get(),
                RAFBlocks.HANGING_VINE_HEAD.get(),
                RAFBlocks.HANGING_VINE_BODY.get()
        );
    }
}
