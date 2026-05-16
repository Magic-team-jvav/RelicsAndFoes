package org.magicteam.relicsandfoes.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.common.data.SoundDefinition;
import org.magicteam.relicsandfoes.RelicsAndFoes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;

public class BedrockSoundConvertor {
    public static Map<String, SoundDefinition> convert() {
        SoundDefinitionsFile soundDefinitionsFile;
        SoundsFile soundsFile;
        Gson gson = new Gson();
        try (InputStream stream = Objects.requireNonNull(BedrockSoundConvertor.class.getResourceAsStream("/sound_definitions.json"))) {
            JsonObject object = gson.fromJson(new InputStreamReader(stream), JsonObject.class);
            soundDefinitionsFile = SoundDefinitionsFile.CODEC.parse(JsonOps.INSTANCE, object).getOrThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (InputStream stream = Objects.requireNonNull(BedrockSoundConvertor.class.getResourceAsStream("/sounds.json"))) {
            JsonObject object = gson.fromJson(new InputStreamReader(stream), JsonObject.class);
            JsonObject entity_sounds = GsonHelper.getAsJsonObject(object, "entity_sounds");
            JsonObject entities = GsonHelper.getAsJsonObject(entity_sounds, "entities");
            soundsFile = SoundsFile.CODEC.parse(JsonOps.INSTANCE, entities).getOrThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, SoundDefinition> sounds = new HashMap<>();
        soundsFile.entities.forEach((entityId, entitySound) -> entitySound.events.forEach((eventType, soundId) -> {
            SoundDefinitionsFile.SoundDefinition soundDefinition = soundDefinitionsFile.soundDefinitions.get(soundId);
            SoundDefinition definition = SoundDefinition.definition();
            for (SoundDefinitionsFile.Sound sound : soundDefinition.sounds) {
                definition.with(sound.unwrap(SoundDefinition.SoundType.EVENT));
            }
            print(soundId, SoundDefinition.SoundType.EVENT);
            sounds.put(soundId, definition);
        }));

        soundDefinitionsFile.soundDefinitions.forEach((soundId, soundDefinition) -> {
            if (sounds.containsKey(soundId)) return;
            SoundDefinition definition = SoundDefinition.definition();
            SoundDefinition.SoundType soundType = soundDefinition.category == SoundDefinitionsFile.Category.MUSIC
                    ? SoundDefinition.SoundType.SOUND
                    : SoundDefinition.SoundType.EVENT;
            for (SoundDefinitionsFile.Sound sound : soundDefinition.sounds) {
                definition.with(sound.unwrap(soundType));
            }
            print(soundId, soundType);
            sounds.put(soundId, definition);
        });

        return sounds;
    }

    private static final Set<String> printed = new HashSet<>();

    private static void print(String soundId, SoundDefinition.SoundType soundType) {
        if (printed.add(soundId)) {
            String fieldName = soundId.toUpperCase(Locale.ROOT).replace(".", "_");
            String methodName = soundType == SoundDefinition.SoundType.SOUND
                    ? "sound(\"" + soundId + "\");"
                    : "event(\"" + soundId + "\");";
            System.out.println("public static final DeferredHolder<SoundEvent, SoundEvent> " + fieldName + " = " + methodName);
        }
    }

    public record SoundsFile(Map<ResourceLocation, Entity> entities) {
        public static final Codec<SoundsFile> CODEC = Codec.unboundedMap(ResourceLocation.CODEC, Entity.CODEC).xmap(SoundsFile::new, SoundsFile::entities);

        public record Entity(float volume, List<Float> pitch, Map<EventType, String> events) {
            public static final Codec<Entity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.lenientOptionalFieldOf("volume", 1F).forGetter(Entity::volume),
                    Codec.FLOAT.listOf().lenientOptionalFieldOf("pitch", List.of(1F, 1F)).forGetter(Entity::pitch),
                    Codec.unboundedMap(EventType.CODEC, Codec.STRING).fieldOf("events").forGetter(Entity::events)
            ).apply(instance, Entity::new));

            public enum EventType implements StringRepresentable {
                HURT,
                DEATH,
                AMBIENT;

                public static final Codec<EventType> CODEC = StringRepresentable.fromEnum(EventType::values);

                @Override
                public String getSerializedName() {
                    return name().toLowerCase(Locale.ROOT);
                }
            }
        }
    }

    public record SoundDefinitionsFile(Map<String, SoundDefinition> soundDefinitions) {
        public static final Codec<SoundDefinitionsFile> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(Codec.STRING, SoundDefinition.CODEC).fieldOf("sound_definitions").forGetter(SoundDefinitionsFile::soundDefinitions)
        ).apply(instance, SoundDefinitionsFile::new));

        public record SoundDefinition(Category category, List<Sound> sounds) {
            public static final Codec<SoundDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Category.CODEC.fieldOf("category").forGetter(SoundDefinition::category),
                    Sound.CODEC.listOf().fieldOf("sounds").forGetter(SoundDefinition::sounds)
            ).apply(instance, SoundDefinition::new));
        }

        public enum Category implements StringRepresentable {
            MUSIC,
            NEUTRAL,
            UI,
            HOSTILE;

            public static final Codec<Category> CODEC = StringRepresentable.fromEnum(Category::values);

            @Override
            public String getSerializedName() {
                return name().toLowerCase(Locale.ROOT);
            }
        }

        public record Sound(String name, boolean stream, float volume, boolean loadOnLowMemory, float pitch) {
            public static final Codec<Sound> COMPLEX_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(Sound::name),
                    Codec.BOOL.lenientOptionalFieldOf("stream", false).forGetter(Sound::stream),
                    Codec.FLOAT.lenientOptionalFieldOf("volume", 1F).forGetter(Sound::volume),
                    Codec.BOOL.lenientOptionalFieldOf("load_on_low_memory", false).forGetter(Sound::loadOnLowMemory),
                    Codec.FLOAT.lenientOptionalFieldOf("pitch", 1F).forGetter(Sound::pitch)
            ).apply(instance, Sound::new));
            public static final Codec<Sound> CODEC = Codec.either(Codec.STRING, COMPLEX_CODEC).xmap(either -> either.map(Sound::new, Function.identity()), Either::right);

            public Sound(String name) {
                this(name, false, 1, false, 1);
            }

            public Sound {
                name = name.replace("sounds/", "");
            }

            public net.neoforged.neoforge.common.data.SoundDefinition.Sound unwrap(net.neoforged.neoforge.common.data.SoundDefinition.SoundType soundType) {
                return net.neoforged.neoforge.common.data.SoundDefinition.Sound
                        .sound(RelicsAndFoes.asResource(name), soundType)
                        .stream(stream)
                        .pitch(pitch)
                        .volume(volume)
                        .preload(loadOnLowMemory);
            }
        }
    }
}
