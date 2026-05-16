package org.magicteam.relicsandfoes.datagen;

import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.RAFBlocks;
import org.magicteam.relicsandfoes.init.RAFDimensions;
import org.magicteam.relicsandfoes.init.RAFItems;
import org.magicteam.relicsandfoes.init.RAFTags;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RAFLanguageProvider extends LanguageProvider {
    protected final boolean isEn;

    public RAFLanguageProvider(PackOutput output, boolean isEn, String locale) {
        super(output, RelicsAndFoes.MODID, locale);
        this.isEn = isEn;
    }

    @Override
    protected void addTranslations() {
        if (isEn) {
            addEnUsTranslations();
        } else {
            addZhCnTranslations();
        }
        addDimension(RAFDimensions.LEVEL, "Relic Land", "古迹大陆");
        addTooltip(RAFItems.PORTABLE_ANCIENT_RELIC_TELEPORTER, "", "长按使用即可往返于古迹大陆与主世界");
    }

    protected void addEnUsTranslations() {
        Consumer<DeferredHolder<?, ?>> action = holder -> toTitleCase(holder.getId().getPath());
        RAFBlocks.BLOCKS.getEntries().forEach(action);
        RAFBlocks.BLOCK_ENTITIES.getEntries().forEach(action);
        RAFItems.ITEMS.getEntries().forEach(action);
        RAFItems.BLOCK_ITEMS.getEntries().forEach(action);

        for (TagKey<?> key : RAFTags.getAllTagsForDataGen()) {
            add(key, toTitleCase(key.location().getPath()));
        }
        for (ResourceKey<Biome> key : RAFDimensions.Biomez.getAllBiomesForDataGen()) {
            addBiome(key, toTitleCase(key.location().getPath()));
        }
    }

    public static String toTitleCase(String raw) {
        return Arrays.stream(raw.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    protected void addZhCnTranslations() {

    }

    public void add(ItemLike key, String en, String zh) {
        add(key.asItem(), isEn ? en : zh);
    }

    public void add(Block key, String en, String zh) {
        add(key, isEn ? en : zh);
    }

    public void add(String key, String en, String zh) {
        add(key, isEn ? en : zh);
    }

    public void add(MobEffect key, String en, String zh) {
        add(key, isEn ? en : zh);
    }

    public void add(TagKey<?> key, String en, String zh) {
        add(key, isEn ? en : zh);
    }

    public void add(EntityType<?> key, String en, String zh) {
        add(key, isEn ? en : zh);
    }

    public void addBiome(ResourceKey<Biome> key, String value) {
        add(Util.makeDescriptionId("biome", key.location()), value);
    }

    public void addDimension(ResourceKey<Level> dimension, String en, String zh) {
        addDimension(dimension, isEn ? en : zh);
    }

    public void addTooltip(ItemLike key, String en, String zh) {
        String id = key.asItem().getDescriptionId();
        add("tooltip." + id + ".0", en, zh);
    }

    public void addTooltip(ItemLike key, String[] en, String[] zh) {
        if (en.length != zh.length) {
            throw new IllegalArgumentException();
        }
        String id = key.asItem().getDescriptionId();
        for (int i = 0; i < en.length; i++) {
            add("tooltip." + id + "." + i, en[i], zh[i]);
        }
    }
}
