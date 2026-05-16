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
import org.magicteam.relicsandfoes.component.LostSoloMelodyComponent;
import org.magicteam.relicsandfoes.init.*;

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
            addEnUsTranslations();
        } else {
            addZhCnTranslations();
        }

        addDimension(RAFDimensions.LEVEL, "Relic Land", "古迹大陆");
        addTooltip(RAFItems.PORTABLE_ANCIENT_RELIC_TELEPORTER, "", "长按使用即可往返于古迹大陆与主世界");
    }

    public static String toTitleCase(String raw) {
        return Arrays.stream(raw.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    protected void addEnUsTranslations() {

    }

    protected void addZhCnTranslations() {
        add("tooltip.lost_solo_melody.0", "一摞神奇的乐谱，有着自动演奏的能力。");
        add("tooltip.lost_solo_melody.1", "当前选中：");
        add("tooltip.lost_solo_melody.2", "自奏取单：");
        add("tooltip.lost_solo_melody.3", "长按使用并滚动鼠标滚轮以选择乐曲");
        add("tooltip.lost_solo_melody.none", "无");

        add(RAFItems.PORTABLE_ANCIENT_RELIC_TELEPORTER.get(), "便携式古迹传送台");
        add(RAFItems.LOST_SOLO_MELODY_ANGEL.get(), "失落的自奏曲-诡像之乐章");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_ANGEL), "血玫追时战 - Beilutal");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_ANGEL), "血玫蔓天 - Beilutal");
        add(RAFItems.LOST_SOLO_MELODY_CONDUCTOR.get(), "失落的自奏曲-魔女之乐章");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_CONDUCTOR), "魔女彩城 - Beilutal");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_CONDUCTOR), "彩汐幻梦 - FRC");
        add(RAFItems.LOST_SOLO_MELODY_KYLIN.get(), "失落的自奏曲-麟兽之乐章");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_KYLIN), "斩仙麟 - Beilutal");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_KYLIN), "绯云漫谷 - Beilutal");
        add(RAFItems.LOST_SOLO_MELODY_MECHANICAL.get(), "失落的自奏曲-钢龙之乐章");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_MECHANICAL_0), "一阶段 - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_MECHANICAL_1), "二阶段 - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_MECHANICAL), "占位符 - FRC");
        add(RAFItems.LOST_SOLO_MELODY_PROTOTYPE.get(), "失落的自奏曲-源械之乐章");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_PROTOTYPE_1), "古迹械啸 - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_PROTOTYPE_4), "圣地的械古战音 - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_PROTOTYPE), "古迹与劲敌·启程歌 - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_PROTOTYPE_WIND), "风啸的古遗迹 - FRC");
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
