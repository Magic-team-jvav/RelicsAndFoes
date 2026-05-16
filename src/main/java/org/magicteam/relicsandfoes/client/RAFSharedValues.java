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
    public static boolean inCrossRealmAncientRuins;
    public static boolean isPrototypeDefeated;

    private static final Object2BooleanMap<Long> prototype = new Object2BooleanOpenHashMap<>();

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
        inCrossRealmAncientRuins = false;
        isPrototypeDefeated = false;

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
        if (immediate || gameTime % 20 == 0) {
            middleX = (Mth.floor(position.x) + 1024) >> 11 << 11;
            middleZ = (Mth.floor(position.z) + 1024) >> 11 << 11;
            inCrossRealmAncientRuins = inTheSunkenExpanse && Mth.lengthSquared(middleX - position.x, middleZ - position.z) <= 160 * 160;
            isPrototypeDefeated = isPrototypeDefeated(middleX, middleZ);
        }
    }
}
