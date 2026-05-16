package org.magicteam.relicsandfoes.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class RAFTags {
    private static List<TagKey<?>> allTags;

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
}
