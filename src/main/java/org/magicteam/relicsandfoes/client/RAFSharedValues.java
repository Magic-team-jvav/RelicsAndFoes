package org.magicteam.relicsandfoes.client;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.magicteam.relicsandfoes.init.RAFDimensions;

public class RAFSharedValues {
    public static boolean available;
    public static LocalPlayer player; // if player not present, all below value defaults
    public static ClientLevel level;
    public static Vec3 position;
    public static int middleX;
    public static int middleZ;
    public static long gameTime;
    public static boolean inRelicLand;
    public static Holder<Biome> biome;
    public static boolean inTheSunkenExpanse;
    public static boolean inTheSeaOfFallingStars;
    public static boolean inThePeachOfBlossomVale;
    public static boolean inTheThornyDreadlands;
    public static boolean inCrossRealmAncientRuins;
    public static boolean isPrototypeDefeated;

    private static final Object2BooleanMap<Long> prototype = new Object2BooleanOpenHashMap<>();

    public record BiomeFog(float nearPlaneNone, float nearPlaneWater, float farPlaneWater) {
        public static final BiomeFog SUNKEN_EXPANSE = new BiomeFog(0, 6, 15);

        public static BiomeFog targetFog;
        public static float prevNearPlaneNone;
        public static float prevNearPlaneWater;
        public static float prevFarPlaneWater;
        public static float fogBlend;
        public static float paramBlend;

        public static @Nullable BiomeFog get() {
            if (inTheSunkenExpanse) return SUNKEN_EXPANSE;
            return null;
        }

        public static void resetBlend() {
            targetFog = null;
            prevNearPlaneNone = 0;
            prevNearPlaneWater = 0;
            prevFarPlaneWater = 0;
            fogBlend = 0;
            paramBlend = 0;
        }
    }

    public static boolean isPrototypeDefeated(int middleX, int middleZ) {
        return prototype.getBoolean(ChunkPos.asLong(middleX, middleZ));
    }

    public static void reset() {
        available = false;
        level = null;
        position = null;
        middleX = 0;
        middleZ = 0;
        gameTime = 0;
        inRelicLand = false;
        biome = null;
        inTheSunkenExpanse = false;
        inTheSeaOfFallingStars = false;
        inThePeachOfBlossomVale = false;
        inTheThornyDreadlands = false;
        inCrossRealmAncientRuins = false;
        isPrototypeDefeated = false;
        BiomeFog.resetBlend();

        prototype.clear();
    }

    public static void update(@Nullable LocalPlayer localPlayer, boolean immediate) {
        player = localPlayer;
        if (player == null) {
            available = false;
            return;
        }
        available = true;
        level = player.clientLevel;
        position = player.position();
        gameTime = level.getGameTime();
        inRelicLand = level.dimension() == RAFDimensions.LEVEL;
        biome = level.getBiome(player.blockPosition());
        inTheSunkenExpanse = biome.is(RAFDimensions.Biomez.THE_SUNKEN_EXPANSE);
        inTheSeaOfFallingStars = biome.is(RAFDimensions.Biomez.THE_SEA_OF_FALLING_STARS);
        inThePeachOfBlossomVale = biome.is(RAFDimensions.Biomez.THE_PEACH_BLOSSOM_VALE);
        inTheThornyDreadlands = biome.is(RAFDimensions.Biomez.THE_THORNY_DREADLANDS);
        if (immediate || gameTime % 20 == 0) {
            middleX = (Mth.floor(position.x) + 1024) >> 11 << 11;
            middleZ = (Mth.floor(position.z) + 1024) >> 11 << 11;
            inCrossRealmAncientRuins = inTheSunkenExpanse && Mth.lengthSquared(middleX - position.x, middleZ - position.z) <= 160 * 160;
            isPrototypeDefeated = isPrototypeDefeated(middleX, middleZ);
        }
    }
}
