package org.magicteam.relicsandfoes.client;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.RAFDimensions;
import org.mesdag.particlestorm.PSGameClient;
import org.mesdag.particlestorm.particle.ParticleEmitter;

import static org.magicteam.relicsandfoes.client.RAFSharedValues.*;

@Mod(value = RelicsAndFoes.MODID, dist = Dist.CLIENT)
public final class RelicsAndFoesClient {
    public RelicsAndFoesClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static float timeOfDayLow(float original) {
        return inRelicLand ? 0.3F : original;
    }

    private static final RandomSource random = RandomSource.create();

    public static void biomeParticles() {
        if (RAFClientConfigs.biomeParticles) {
            if (gameTime % 2 == 0) {
                if (inTheSunkenExpanse) {
                    if (inCrossRealmAncientRuins && !isPrototypeDefeated) {
                        addEmitter(
                                position.x + nextBetweenInclusive(random, -18, 8),
                                position.y + nextBetweenInclusive(random, -5, 8),
                                position.z + nextBetweenInclusive(random, -18, 8),
                                "city_wind"
                        );
                        addEmitter(position, "city_petal_atlas");
                    }
                }
            }
            if (gameTime % 20 == 0) {
                if (biome.is(RAFDimensions.Biomez.THE_SEA_OF_FALLING_STARS)) {

                } else if (inTheSunkenExpanse) {
                    RandomSource random = player.getRandom();
                    if (!inCrossRealmAncientRuins || isPrototypeDefeated) {
                        for (int i = 0; i < 5; i++) {
                            addEmitter(
                                    position.x + nextScale(random, 20),
                                    position.y + nextScale(random, 7),
                                    position.z + nextScale(random, 20),
                                    "the_sunken_expanse_light_circle"
                            );
                        }
                        if (position.y <= 75) {
                            for (int i = 0; i < 5; i++) {
                                addEmitter(
                                        position.x + nextScale(random, 20),
                                        position.y + nextScale(random, 7),
                                        position.z + nextScale(random, 20),
                                        "the_sunken_expanse_fallen_leaves"
                                );
                            }
                        }
                    }
                } else if (biome.is(RAFDimensions.Biomez.THE_PEACH_BLOSSOM_VALE)) {

                }
            }
        }
        if (gameTime % 60 == 0) {
            if (inTheSunkenExpanse && (!inCrossRealmAncientRuins || isPrototypeDefeated) && (RAFClientConfigs.mistParticles || RAFClientConfigs.animalParticles)) label0:{
                if (position.y <= 35) break label0;
                int px = Mth.floor(position.x) >> 3 << 3;
                int pz = Mth.floor(position.z) >> 3 << 3;
                BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                for (int x = -56; x < 64; x += 8) {
                    for (int z = -56; z < 64; z += 8) {
                        int bx = px + x;
                        int bz = pz + z;
                        random.setSeed(((long) position.x + bx) * 10000L + ((long) position.z + bz));
                        if (RAFClientConfigs.animalParticles && position.y < 75 && chance(random, 10)) {
                            int height = getHeight(mutable, bx, bz);
                            if (Mth.lengthSquared(x, height - position.y, z) < 40 * 40) {
                                addEmitter(bx, height + 2, bz, "butterfly_yellow");
                            }
                        }
                        if (RAFClientConfigs.mistParticles) {
                            double dist = Mth.lengthSquared(x, z);
                            if (withinExclusive(26 * 26, dist, 40 * 40) && chance(random, 3)) {
                                if (position.y < 75) {
                                    int height = getHeight(mutable, bx, bz);
                                    if (height <= 60 && withinExclusive(26 * 26, Mth.lengthSquared(x, height - position.y, z), 40 * 40)) {
                                        addEmitter(bx, height + 1, bz, "new_mist_0");
                                    }
                                } else {
                                    addEmitter(bx, position.y + nextScale(random, 10), bz, "new_mist_big_plains");
                                }
                            }
                            if (dist > 24 && chance(random, 20)) {
                                int y = nextBetweenInclusive(random, 80, 100);
                                if (Mth.lengthSquared(x, y - position.y, z) > 36 * 36) {
                                    addEmitter(bx, y, bz, "clouds");
                                }
                            }
                        }
                    }
                }
                random.setSeed(0);
            }
            if (biome.is(RAFDimensions.Biomez.THE_THORNY_DREADLANDS)) {

            }
            if (biome.is(RAFDimensions.Biomez.THE_SEA_OF_FALLING_STARS)) {

            }
            if (biome.is(RAFDimensions.Biomez.THE_PEACH_BLOSSOM_VALE)) {

            }
        }
    }

    public static void addEmitter(double x, double y, double z, String path) {
        PSGameClient.LOADER.addEmitter(new ParticleEmitter(level, new Vec3(x, y, z), RelicsAndFoes.asResource(path)));
    }

    public static void addEmitter(Vec3 pos, String path) {
        PSGameClient.LOADER.addEmitter(new ParticleEmitter(level, pos, RelicsAndFoes.asResource(path)));
    }

    public static int getHeight(BlockPos.MutableBlockPos mutable, int bx, int bz) {
        mutable.setX(bx).setZ(bz);
        for (int y = Mth.floor(player.getY()) + 16; y > level.getMinBuildHeight(); y--) {
            if (level.getBlockState(mutable.setY(y)).canOcclude()) {
                return y;
            }
        }
        return level.getMinBuildHeight();
    }

    public static boolean chance(RandomSource random, int denominator) {
        return random.nextInt(denominator) == 0;
    }

    public static boolean withinExclusive(double min, double value, double max) {
        return value > min && value < max;
    }

    public static boolean withinInclusive(double min, double value, double max) {
        return value >= min && value <= max;
    }

    public static int nextBetweenInclusive(RandomSource random, int min, int max) {
        return random.nextIntBetweenInclusive(min, max);
    }

    public static int nextScale(RandomSource random, int scale) {
        return random.nextIntBetweenInclusive(-scale, scale);
    }

    public static double nextBetweenInclusive(RandomSource random, double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    public static double nextScale(RandomSource random, double scale) {
        return (random.nextDouble() - 0.5) * (scale + scale);
    }
}
