package org.magicteam.relicsandfoes.world.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import net.neoforged.neoforge.common.TranslatableEnum;
import org.jetbrains.annotations.Nullable;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.RAFBlocks;
import org.magicteam.relicsandfoes.init.RAFItems;
import org.magicteam.relicsandfoes.init.RAFTags;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class RAFCreativeModeTab extends CreativeModeTab {
    protected Collection<ItemStack> displayItemsCopy;
    protected List<Collection<ItemStack>> tabs = List.of();

    public RAFCreativeModeTab() {
        super(
                Row.TOP,
                0,
                Type.CATEGORY,
                Component.translatable("itemGroup.relics_and_foes"),
                RAFItems.PORTABLE_ANCIENT_RELIC_TELEPORTER::toStack,
                RelicsAndFoes::generator,
                null,
                false,
                89,
                Builder.CREATIVE_INVENTORY_TABS_IMAGE,
                4210752,
                -2130706433,
                List.of(),
                List.of()
        );
    }

    @Override
    public boolean hasAnyItems() {
        return true;
    }

    @Override
    public void buildContents(ItemDisplayParameters parameters) {
        super.buildContents(parameters);
        if (displayItemsCopy == null) {
            this.displayItemsCopy = displayItems;
        }
        ImmutableList.Builder<Collection<ItemStack>> builder = ImmutableList.builder();
        for (int i = 0; i < SideTab.VALUES.length; i++) {
            builder.add(ItemStackLinkedSet.createTypeAndComponentsSet());
        }
        this.tabs = builder.build();
        Collection<ItemStack> others = getDisplayItems(SideTab.RELIC_LAND);
        next:
        for (ItemStack displayItem : displayItemsCopy) {
            for (SideTab tab : SideTab.VALUES) {
                if (tab == SideTab.RELIC_LAND) continue;
                assert tab.key != null;
                if (displayItem.is(tab.key)) {
                    getDisplayItems(tab).add(displayItem);
                    continue next;
                }
            }
            others.add(displayItem);
        }
        this.displayItems = others;
    }

    public Collection<ItemStack> getDisplayItems(SideTab tab) {
        return tabs.get(tab.ordinal());
    }

    public void setDisplayItems(SideTab tab) {
        this.displayItems = getDisplayItems(tab);
    }

    public enum SideTab implements TranslatableEnum {
        RELIC_LAND(null, RAFBlocks.ABANDONED_TABLET::toStack),
        HILLSIDE_XIE_BEAST(RAFTags.Items.HILLSIDE_XIE_BEAST, RAFItems.LOST_SOLO_MELODY_KYLIN_MOVEMENT::toStack),
        RUINED_HALL_EERIE_STATUE(RAFTags.Items.RUINED_HALL_EERIE_STATUE, RAFItems.LOST_SOLO_MELODY_ANGEL_MOVEMENT::toStack),
        DEMON_CASTLE_PUPPET(RAFTags.Items.DEMON_CASTLE_PUPPET, RAFItems.LOST_SOLO_MELODY_CONDUCTOR_MOVEMENT::toStack),
        FALLEN_CITY_REMNANT_GEAR(RAFTags.Items.FALLEN_CITY_REMNANT_GEAR, RAFItems.LOST_SOLO_MELODY_PROTOTYPE_MOVEMENT::toStack),
        FLOATING_FORT_IRONWING(RAFTags.Items.FLOATING_FORT_IRONWING, RAFItems.LOST_SOLO_MELODY_MECHANICAL_MOVEMENT::toStack);

        public static final SideTab[] VALUES = values();

        public final @Nullable TagKey<Item> key;
        private final Supplier<ItemStack> iconSupplier;

        private ItemStack icon;

        SideTab(@Nullable TagKey<Item> key, Supplier<ItemStack> iconSupplier) {
            this.key = key;
            this.iconSupplier = iconSupplier;
        }

        @Override
        public Component getTranslatedName() {
            return Component.translatable(getDescriptionId());
        }

        public String getDescriptionId() {
            return "side_tab.relics_and_foes." + name().toLowerCase(Locale.ROOT);
        }

        public ItemStack getIcon() {
            if (icon == null) {
                this.icon = iconSupplier.get();
            }
            return icon;
        }
    }
}
