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
import org.mesdag.particlestorm.particle.MolangParticleEngine;
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
        if (gameTime % 2 == 0) {
            if (inTheSunkenExpanse) {
                if (RAFClientConfigs.biomeParticles && inCrossRealmAncientRuins && !isPrototypeDefeated) {
                    RandomSource random = player.getRandom();
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
            if (inTheSeaOfFallingStars) {
                RandomSource random = player.getRandom();
                if (RAFClientConfigs.biomeParticles) {
                    if (position.y <= 80) {
                        for (int i = 0; i < 5; i++) {
                            addEmitter(
                                    position.x + nextScale(random, 20),
                                    position.y + nextScale(random, 7),
                                    position.z + nextScale(random, 20),
                                    "fallen_leaves_lukewarm_ocean"
                            );
                        }
                    }
                    if (position.y <= 165) {
                        addEmitter(
                                position.x + nextBetweenInclusive(random, -128, 64),
                                5,
                                position.z + nextBetweenInclusive(random, -128, 64),
                                "meteor"
                        );
                    }
                }
                if (RAFClientConfigs.mistParticles) {
                    for (int i = 0; i < 2; i++) {
                        double[] pos = getCirclePos(random, random.nextIntBetweenInclusive(128, 148));
                        addEmitter(pos[0], 80, pos[1], "aurora");
                    }
                }
            } else if (inTheSunkenExpanse) {
                RandomSource random = player.getRandom();
                if (RAFClientConfigs.biomeParticles && (!inCrossRealmAncientRuins || isPrototypeDefeated)) {
                    for (int i = 0; i < 5; i++) {
                        addEmitter(
                                position.x + nextScale(random, 20),
                                position.y + nextScale(random, 7),
                                position.z + nextScale(random, 20),
                                "light_circle_plains"
                        );
                    }
                    if (position.y <= 75) {
                        for (int i = 0; i < 5; i++) {
                            addEmitter(
                                    position.x + nextScale(random, 20),
                                    position.y + nextScale(random, 7),
                                    position.z + nextScale(random, 20),
                                    "fallen_leaves_plains"
                            );
                        }
                    }
                }
            } else if (inThePeachOfBlossomVale) {
                if (RAFClientConfigs.biomeParticles) {
                    RandomSource random = player.getRandom();
                    for (int i = 0; i < 3; i++) {
                        addEmitter(
                                position.x + nextScale(random, 20),
                                position.y + nextScale(random, 7),
                                position.z + nextScale(random, 20),
                                "light_circle_forest_hills"
                        );
                    }
                    if (position.y <= 75) {
                        for (int i = 0; i < 7; i++) {
                            addEmitter(
                                    position.x + nextScale(random, 20),
                                    position.y + nextScale(random, 7),
                                    position.z + nextScale(random, 20),
                                    "fallen_leaves_forest_hills"
                            );
                        }
                    }
                }
            } else if (inTheThornyDreadlands) {
                RandomSource random = player.getRandom();
                for (int i = 0; i < 3; i++) {
                    addEmitter(
                            position.x + nextScale(random, 20),
                            position.y + nextScale(random, 7),
                            position.z + nextScale(random, 20),
                            "light_circle_forest_hills"
                    );
                }
                for (int i = 0; i < 10; i++) {
                    addEmitter(
                            position.x + nextScale(random, 6),
                            position.y + nextScale(random, 4),
                            position.z + nextScale(random, 6),
                            "light_circle_birch_forest_hills"
                    );
                }
                for (int i = 0; i < 8; i++) {
                    addEmitter(
                            position.x + nextScale(random, 20),
                            position.y + nextBetweenInclusive(random, -4, 2),
                            position.z + nextScale(random, 20),
                            "light_circle_birch_forest_hills2"
                    );
                }
                if (position.y <= 110) {
                    for (int i = 0; i < 5; i++) {
                        addEmitter(
                                position.x + nextScale(random, 20),
                                position.y + nextScale(random, 7),
                                position.z + nextScale(random, 20),
                                "fallen_leaves_plains"
                        );
                    }
                }
            }
        }
        if (gameTime % 60 == 0) {
            if (inTheSunkenExpanse &&
                    (!inCrossRealmAncientRuins || isPrototypeDefeated) &&
                    (RAFClientConfigs.mistParticles || RAFClientConfigs.animalParticles) &&
                    position.y > 35
            ) {
                int px = Mth.floor(position.x) >> 3 << 3;
                int pz = Mth.floor(position.z) >> 3 << 3;
                BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                for (int x = -56; x < 64; x += 8) {
                    for (int z = -56; z < 64; z += 8) {
                        int bx = px + x;
                        int bz = pz + z;
                        random.setSeed((long) bx * 10000L + (long) bz);
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
            } else if (inTheThornyDreadlands && RAFClientConfigs.mistParticles && position.y > 35) {
                int px = Mth.floor(position.x) >> 3 << 3;
                int pz = Mth.floor(position.z) >> 3 << 3;
                BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                for (int x = -40; x < 48; x += 8) {
                    for (int z = -40; z < 48; z += 8) {
                        int bx = px + x;
                        int bz = pz + z;
                        random.setSeed((long) bx * 10000L + (long) bz);
                        if (withinExclusive(18 * 18, Mth.lengthSquared(x, z), 54 * 54) && chance(random, 3)) {
                            if (position.y < 75) {
                                int height = getHeight(mutable, bx, bz);
                                if (height <= 60 && withinExclusive(18 * 18, Mth.lengthSquared(x, height - position.y, z), 54 * 54)) {
                                    addEmitter(bx, height + 2.5, bz, "new_mist_moved");
                                }
                            }
                        }
                    }
                }
                random.setSeed(0);
            } else if (inTheSeaOfFallingStars) {
                if (RAFClientConfigs.mistParticles && position.y > 10) {
                    int px = Mth.floor(position.x) >> 3 << 3;
                    int pz = Mth.floor(position.z) >> 3 << 3;
                    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                    for (int x = -40; x < 48; x += 8) {
                        for (int z = -40; z < 48; z += 8) {
                            int bx = px + x;
                            int bz = pz + z;
                            random.setSeed((long) bx * 10000L + (long) bz);
                            if (withinExclusive(24 * 24, Mth.lengthSquared(x, z), 54 * 54) && chance(random, 4)) {
                                if (position.y < 90) {
                                    int height = getHeight(mutable, bx, bz);
                                    if (withinInclusive(30, height, 150) && withinExclusive(18 * 18, Mth.lengthSquared(x, height - position.y, z), 54 * 54)) {
                                        addEmitter(bx, height + 1, bz, "new_mist_0");
                                    }
                                }
                            }
                        }
                    }
                    random.setSeed(0);
                }
                if (RAFClientConfigs.biomeParticles) {
                    for (int i = 0; i < 3; i++) {
                        addEmitter(
                                position.x + nextScale(random, 80.0),
                                16,
                                position.z + nextScale(random, 80.0),
                                "water_star"
                        );
                    }
                }
            } else if (inThePeachOfBlossomVale && (RAFClientConfigs.mistParticles || RAFClientConfigs.animalParticles) && position.y > 35) {
                int px = Mth.floor(position.x) >> 3 << 3;
                int pz = Mth.floor(position.z) >> 3 << 3;
                BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                for (int x = -56; x < 64; x += 8) {
                    for (int z = -56; z < 64; z += 8) {
                        int bx = px + x;
                        int bz = pz + z;
                        if (RAFClientConfigs.mistParticles) {
                            if (withinExclusive(26 * 26, Mth.lengthSquared(x, z), 40 * 40)) {
                                random.setSeed((long) bx * 10000L + (long) bz);
                                if (chance(random, 3) && position.y < 80) {
                                    int height = getHeight(mutable, bx, bz);
                                    if (height <= 72 && withinExclusive(26 * 26, Mth.lengthSquared(x, height - position.y, z), 54 * 54)) {
                                        addEmitter(bx, height + 1, bz, "new_mist_0");
                                    }
                                }
                            }
                            if (withinExclusive(44 * 44, Mth.lengthSquared(x, z), 60 * 60)) {
                                random.setSeed((long) bx * 10000L + (long) bz);
                                if (random.nextInt(10) >= 7) {
                                    int height = getHeight(mutable, bx, bz);
                                    if (height <= 80) {
                                        addEmitter(bx, height + 3, bz, "new_mist_big_plains");
                                    }
                                }
                            }
                        }
                        if (RAFClientConfigs.animalParticles && Mth.lengthSquared(x, z) < 40 * 40) {
                            random.setSeed((long) bx * 10000L + (long) bz);
                            if (chance(random, 10) && position.y < 80) {
                                int height = getHeight(mutable, bx, bz);
                                if (height <= 68 && Mth.lengthSquared(x, height - position.y, z) < 40 * 40) {
                                    for (int i = 0; i < 7; i++) {
                                        addEmitter(
                                                bx + nextScale(random, 3),
                                                height + 1.5 + nextBetweenInclusive(random, 0, 6),
                                                bz + nextScale(random, 3),
                                                "butterfly_light_yellow"
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
                random.setSeed(0);
            }
        }
    }

    public static void addEmitter(double x, double y, double z, String path) {
        addEmitter(new Vec3(x, y, z), path);
    }

    public static void addEmitter(Vec3 pos, String path) {
        ParticleEmitter emitter = new ParticleEmitter(level, pos, RelicsAndFoes.asResource(path));
        emitter.hideOutline = true;
        MolangParticleEngine.INSTANCE.addEmitter(emitter);
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

    public static double[] getCirclePos(RandomSource random, double r) {
        double rot = random.nextDouble() * Math.TAU;
        return new double[]{
                Math.fma(Math.sin(rot), r, position.x),
                Math.fma(Math.cos(rot), r, position.z)
        };
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
