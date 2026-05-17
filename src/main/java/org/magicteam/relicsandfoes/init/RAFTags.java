package org.magicteam.relicsandfoes.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import org.magicteam.relicsandfoes.RelicsAndFoes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class RAFTags {
    private static List<TagKey<?>> allTags;

    static {
        Blocks.init();
        Items.init();
    }

    public static List<TagKey<?>> getAllTagsForDataGen() {
        if (DatagenModLoader.isRunningDataGen()) {
            return Objects.requireNonNullElseGet(allTags, List::of);
        }
        throw new UnsupportedOperationException("Can not get all tags");
    }

    private static <T> TagKey<T> register(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation location) {
        TagKey<T> key = TagKey.create(registryKey, location);
        if (DatagenModLoader.isRunningDataGen()) {
            if (allTags == null) {
                allTags = new ArrayList<>();
            }
            allTags.add(key);
        }
        return key;
    }

    public static final class Blocks {
        public static final TagKey<Block> HILLSIDE_XIE_BEAST = raf("hillside_xie_beast"); // 蓬山獬兽
        public static final TagKey<Block> RUINED_HALL_EERIE_STATUE = raf("ruined_hall_eerie_statue"); // 残殿诡像
        public static final TagKey<Block> DEMON_CASTLE_PUPPET = raf("demon_castle_puppet"); // 魔堡戏偶
        public static final TagKey<Block> FALLEN_CITY_REMNANT_GEAR = raf("fallen_city_remnant_gear"); // 沦城遗械
        public static final TagKey<Block> FLOATING_FORT_IRONWING = raf("floating_fort_ironwing"); // 浮垒铁翼

        private static TagKey<Block> raf(String path) {
            return register(Registries.BLOCK, RelicsAndFoes.asResource(path));
        }

        private static void init() {}
    }

    public static final class Items {
        public static final TagKey<Item> HILLSIDE_XIE_BEAST = raf("hillside_xie_beast"); // 蓬山獬兽
        public static final TagKey<Item> RUINED_HALL_EERIE_STATUE = raf("ruined_hall_eerie_statue"); // 残殿诡像
        public static final TagKey<Item> DEMON_CASTLE_PUPPET = raf("demon_castle_puppet"); // 魔堡戏偶
        public static final TagKey<Item> FALLEN_CITY_REMNANT_GEAR = raf("fallen_city_remnant_gear"); // 沦城遗械
        public static final TagKey<Item> FLOATING_FORT_IRONWING = raf("floating_fort_ironwing"); // 浮垒铁翼

        private static TagKey<Item> raf(String path) {
            return register(Registries.ITEM, RelicsAndFoes.asResource(path));
        }

        private static void init() {}
    }
}
