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
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.component.LostSoloMelodyComponent;
import org.magicteam.relicsandfoes.init.*;
import org.magicteam.relicsandfoes.mixin.LanguageProviderAccessor;
import org.magicteam.relicsandfoes.world.item.RAFCreativeModeTab;

import java.util.Arrays;
import java.util.Locale;
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
            RAFBlocks.BLOCKS.getEntries().forEach(holder -> add(holder.get(), toTitleCase(holder.getId().getPath())));
            RAFItems.ITEMS.getEntries().forEach(holder -> add(holder.get(), toTitleCase(holder.getId().getPath())));
            for (TagKey<?> key : RAFTags.getAllTagsForDataGen()) {
                add(key, toTitleCase(key.location().getPath()));
            }
            for (ResourceKey<Biome> key : RAFDimensions.Biomez.getAllBiomesForDataGen()) {
                addBiome(key, toTitleCase(key.location().getPath()));
            }
            for (RAFCreativeModeTab.SideTab tab : RAFCreativeModeTab.SideTab.VALUES) {
                add(tab.getDescriptionId(), toTitleCase(tab.name().toLowerCase(Locale.ROOT)));
            }
            addEnUsTranslations();
        } else {
            addZhCnTranslations();
        }

        add("itemGroup.relics_and_foes", "Relics And Foes", "古迹与劲敌");
        addDimension(RAFDimensions.LEVEL, "Relic Land", "古迹大陆");

        add("tooltip.lost_solo_melody.0", "A stack of magical sheet music capable of auto-playing.", "一摞神奇的乐谱，有着自动演奏的能力。");
        add("tooltip.lost_solo_melody.1", "Selected: %s", "当前选中：%s");
        add("tooltip.lost_solo_melody.2", "Melody List: ", "自奏曲单：");
        add("tooltip.lost_solo_melody.3", "Hold use and scroll mouse wheel to switch tunes", "长按使用并滚动鼠标滚轮以选择乐曲");
        add("tooltip.lost_solo_melody.none", "None", "无");
        add("gui.select_music.stop", "Stop Playing", "停止播放");

        addTooltip(RAFItems.PORTABLE_ANCIENT_RELIC_TELEPORTER, "Hold right-click to travel between Relic Land and Overworld", "长按使用即可往返于古迹大陆与主世界");
    }

    public static String toTitleCase(String raw) {
        return Arrays.stream(raw.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    protected void addEnUsTranslations() {
        add(RAFItems.LOST_SOLO_MELODY_ANGEL_MOVEMENT.get(), "Lost Solo Melody - Angel's Movement");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_ANGEL), "Bloodrose Timechaser - Beilutal");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_ANGEL), "Crimson Rose Skies - Beilutal");
        add(RAFItems.LOST_SOLO_MELODY_CONDUCTOR_MOVEMENT.get(), "Lost Solo Melody - Conductor's Movement");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_CONDUCTOR), "Witch's Colorful Citadel - Beilutal");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_CONDUCTOR), "Color Tide Fantasy - FRC");
        add(RAFItems.LOST_SOLO_MELODY_KYLIN_MOVEMENT.get(), "Lost Solo Melody - Kylin's Movement");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_KYLIN), "Fairy Kylin Slayer - Beilutal");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_KYLIN), "Scarlet Cloud Valley - Beilutal");
        add(RAFItems.LOST_SOLO_MELODY_MECHANICAL_MOVEMENT.get(), "Lost Solo Melody - Mechanical's Movement");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_MECHANICAL_0), "Phase 1 - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_MECHANICAL_1), "Phase 2 - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_MECHANICAL), "Placeholder - FRC");
        add(RAFItems.LOST_SOLO_MELODY_PROTOTYPE_MOVEMENT.get(), "Lost Solo Melody - Prototype's Movement");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_PROTOTYPE_1), "Ancient Machine's Roar - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_PROTOTYPE_4), "Sacred Battle Hymn of Ancient Machines - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_PROTOTYPE), "Relics and Rivals · Journey - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_PROTOTYPE_WIND), "Wind Howl Ancient Ruins - FRC");
    }

    protected void addZhCnTranslations() {
        add(RAFItems.LOST_SOLO_MELODY_ANGEL_MOVEMENT.get(), "失落的自奏曲-诡像之乐章");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_ANGEL), "血玫追时战 - Beilutal");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_ANGEL), "血玫蔓天 - Beilutal");
        add(RAFItems.LOST_SOLO_MELODY_CONDUCTOR_MOVEMENT.get(), "失落的自奏曲-魔女之乐章");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_CONDUCTOR), "魔女彩城 - Beilutal");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_CONDUCTOR), "彩汐幻梦 - FRC");
        add(RAFItems.LOST_SOLO_MELODY_KYLIN_MOVEMENT.get(), "失落的自奏曲-麟兽之乐章");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_KYLIN), "斩仙麟 - Beilutal");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_KYLIN), "绯云漫谷 - Beilutal");
        add(RAFItems.LOST_SOLO_MELODY_MECHANICAL_MOVEMENT.get(), "失落的自奏曲-钢龙之乐章");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_MECHANICAL_0), "一阶段 - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_MECHANICAL_1), "二阶段 - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_MECHANICAL), "占位符 - FRC");
        add(RAFItems.LOST_SOLO_MELODY_PROTOTYPE_MOVEMENT.get(), "失落的自奏曲-源械之乐章");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_PROTOTYPE_1), "古迹械啸 - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BOSS_PROTOTYPE_4), "圣地的械古战音 - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_PROTOTYPE), "古迹与劲敌·启程歌 - FRC");
        add(LostSoloMelodyComponent.getDescriptionId(RAFMusics.BIOME_PROTOTYPE_WIND), "风啸的古遗迹 - FRC");

        addBiome(RAFDimensions.Biomez.THE_SUNKEN_EXPANSE, "沉沦原野");
        addBiome(RAFDimensions.Biomez.THE_MISTY_SNOWY_PEAKS, "迷雾雪峰");
        addBiome(RAFDimensions.Biomez.THE_RUST_SILENT_CITY, "锈寂城");
        addBiome(RAFDimensions.Biomez.THE_AZURE_SEA, "蔚蓝海");
        addBiome(RAFDimensions.Biomez.THE_THORNY_DREADLANDS, "棘悚之地");
        addBiome(RAFDimensions.Biomez.THE_PEACH_BLOSSOM_VALE, "桃源谷");
        addBiome(RAFDimensions.Biomez.THE_SEA_OF_FALLING_STARS, "飞星幻海");
        addBiome(RAFDimensions.Biomez.THE_FOREST_OF_DUSK, "落日之森");

        add(RAFCreativeModeTab.SideTab.RELIC_LAND.getDescriptionId(), "古迹大陆");
        addAll("蓬山獬兽",
                Tags.getTagTranslationKey(RAFTags.Blocks.HILLSIDE_XIE_BEAST),
                Tags.getTagTranslationKey(RAFTags.Items.HILLSIDE_XIE_BEAST),
                RAFCreativeModeTab.SideTab.HILLSIDE_XIE_BEAST.getDescriptionId()
        );
        addAll("残殿诡像",
                Tags.getTagTranslationKey(RAFTags.Blocks.RUINED_HALL_EERIE_STATUE),
                Tags.getTagTranslationKey(RAFTags.Items.RUINED_HALL_EERIE_STATUE),
                RAFCreativeModeTab.SideTab.RUINED_HALL_EERIE_STATUE.getDescriptionId()
        );
        addAll("魔堡戏偶",
                Tags.getTagTranslationKey(RAFTags.Blocks.MAGIC_CASTLE_PUPPET),
                Tags.getTagTranslationKey(RAFTags.Items.MAGIC_CASTLE_PUPPET),
                RAFCreativeModeTab.SideTab.DEMON_CASTLE_PUPPET.getDescriptionId()
        );
        addAll("沦城遗械",
                Tags.getTagTranslationKey(RAFTags.Blocks.FALLEN_CITY_REMNANT_GEAR),
                Tags.getTagTranslationKey(RAFTags.Items.FALLEN_CITY_REMNANT_GEAR),
                RAFCreativeModeTab.SideTab.FALLEN_CITY_REMNANT_GEAR.getDescriptionId()
        );
        addAll("浮垒铁翼",
                Tags.getTagTranslationKey(RAFTags.Blocks.FLOATING_FORT_IRONWING),
                Tags.getTagTranslationKey(RAFTags.Items.FLOATING_FORT_IRONWING),
                RAFCreativeModeTab.SideTab.FLOATING_FORT_IRONWING.getDescriptionId()
        );

        add(RAFItems.PORTABLE_ANCIENT_RELIC_TELEPORTER.get(), "便携式古迹传送台");
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

    @Override
    public void add(String key, String value) {
        ((LanguageProviderAccessor) this).getData().put(key, value);
    }

    public void addAll(String value, String... keys) {
        for (String key : keys) {
            add(key, value);
        }
    }
}
