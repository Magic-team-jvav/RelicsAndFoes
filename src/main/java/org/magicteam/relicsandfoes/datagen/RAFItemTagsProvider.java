package org.magicteam.relicsandfoes.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.RAFItems;
import org.magicteam.relicsandfoes.init.RAFTags;

import java.util.concurrent.CompletableFuture;

public class RAFItemTagsProvider extends ItemTagsProvider {
    public RAFItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, RelicsAndFoes.MODID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(RAFTags.Items.HILLSIDE_XIE_BEAST).add(
                RAFItems.LOST_SOLO_MELODY_KYLIN_MOVEMENT.get()
        );
        tag(RAFTags.Items.DEMON_CASTLE_PUPPET).add(
                RAFItems.LOST_SOLO_MELODY_CONDUCTOR_MOVEMENT.get()
        );
        tag(RAFTags.Items.FLOATING_FORT_IRONWING).add(
                RAFItems.LOST_SOLO_MELODY_MECHANICAL_MOVEMENT.get()
        );
        tag(RAFTags.Items.FALLEN_CITY_REMNANT_GEAR).add(
                RAFItems.PORTABLE_ANCIENT_RELIC_TELEPORTER.get(),
                RAFItems.LOST_SOLO_MELODY_PROTOTYPE_MOVEMENT.get()
        );
        tag(RAFTags.Items.RUINED_HALL_EERIE_STATUE).add(
                RAFItems.LOST_SOLO_MELODY_ANGEL_MOVEMENT.get()
        );

        copy(RAFTags.Blocks.FALLEN_CITY_REMNANT_GEAR, RAFTags.Items.FALLEN_CITY_REMNANT_GEAR);
    }
}
