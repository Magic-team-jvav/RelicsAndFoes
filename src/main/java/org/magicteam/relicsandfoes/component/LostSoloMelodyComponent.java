package org.magicteam.relicsandfoes.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record LostSoloMelodyComponent(List<Music> musics, int selected) {
    public static final Codec<LostSoloMelodyComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Music.CODEC.listOf().fieldOf("musics").forGetter(LostSoloMelodyComponent::musics),
            Codec.INT.fieldOf("selected").forGetter(LostSoloMelodyComponent::selected)
    ).apply(instance, LostSoloMelodyComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Music> MUSIC_STREAM_CODEC = StreamCodec.composite(
            SoundEvent.STREAM_CODEC, Music::getEvent,
            ByteBufCodecs.VAR_INT, Music::getMinDelay,
            ByteBufCodecs.VAR_INT, Music::getMaxDelay,
            ByteBufCodecs.BOOL, Music::replaceCurrentMusic,
            Music::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, LostSoloMelodyComponent> STREAM_CODEC = StreamCodec.composite(
            MUSIC_STREAM_CODEC.apply(ByteBufCodecs.list()), LostSoloMelodyComponent::musics,
            ByteBufCodecs.VAR_INT, LostSoloMelodyComponent::selected,
            LostSoloMelodyComponent::new
    );

    public LostSoloMelodyComponent select(int index) {
        return new LostSoloMelodyComponent(musics, limit(index));
    }

    public int limit(int index) {
        return Mth.clamp(index, -1, musics.size() - 1);
    }

    public @Nullable Music getMusic(int index) {
        int limit = limit(index);
        if (limit == -1) {
            return null;
        }
        return musics.get(limit);
    }

    public void appendTooltips(List<Component> tooltips) {
        tooltips.add(Component.translatable("tooltip.lost_solo_melody.0").withStyle(ChatFormatting.GRAY));
        Component current;
        if (selected < 0) {
            current = Component.translatable("tooltip.lost_solo_melody.none");
        } else {
            current = Component.translatable(getDescriptionId(musics.get(selected)));
        }
        tooltips.add(Component.translatable("tooltip.lost_solo_melody.1", current).withStyle(ChatFormatting.BLUE));
        tooltips.add(Component.translatable("tooltip.lost_solo_melody.2").withStyle(ChatFormatting.GRAY));
        for (int i = 0; i < musics.size(); i++) {
            tooltips.add(Component.translatable(getDescriptionId(musics.get(i))).withColor(i % 2 == 0 ? 0xEFCE16 : 0xFFAA00));
        }
        tooltips.add(Component.translatable("tooltip.lost_solo_melody.3").withStyle(ChatFormatting.GRAY));
    }

    public static String getDescriptionId(Music music) {
        ResourceKey<SoundEvent> key = music.getEvent().getKey();
        if (key == null) {
            return "music.relics_and_foes.unknown";
        }
        ResourceLocation location = key.location();
        return "music." + location.getNamespace() + "." + location.getPath().replace("music.", "");
    }
}
