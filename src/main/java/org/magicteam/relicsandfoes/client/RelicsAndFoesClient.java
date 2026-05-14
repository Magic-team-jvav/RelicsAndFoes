package org.magicteam.relicsandfoes.client;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
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

import static org.magicteam.relicsandfoes.client.RAFClientEvents.*;

@Mod(value = RelicsAndFoes.MODID, dist = Dist.CLIENT)
public final class RelicsAndFoesClient {
    public RelicsAndFoesClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static float timeOfDayLow(float original) {
        return inRelicLand ? 0.3F : original;
    }

    public static void biomeParticles() {
        Level level = player.level();
        long gameTime = level.getGameTime();
        if (gameTime % 20 == 0) {
            Vec3 pos = player.position();
            RandomSource random = player.getRandom();
            if (biome.is(RAFDimensions.Biomez.THE_SEA_OF_FALLING_STARS)) {

            } else if (inTheSunkenExpanse) {
                for (int i = 0; i < 5; i++) {
                    PSGameClient.LOADER.addEmitter(new ParticleEmitter(
                            level,
                            new Vec3(
                                    pos.x + random.nextIntBetweenInclusive(-20, 20),
                                    pos.y + random.nextIntBetweenInclusive(-7, 7),
                                    pos.z + random.nextIntBetweenInclusive(-20, 20)
                            ),
                            RelicsAndFoes.asResource("the_sunken_expanse_light_circle")
                    ), false);
                }
                if (pos.y <= 75) {
                    for (int i = 0; i < 5; i++) {
                        PSGameClient.LOADER.addEmitter(new ParticleEmitter(
                                level,
                                new Vec3(
                                        pos.x + random.nextIntBetweenInclusive(-20, 20),
                                        pos.y + random.nextIntBetweenInclusive(-7, 7),
                                        pos.z + random.nextIntBetweenInclusive(-20, 20)
                                ),
                                RelicsAndFoes.asResource("the_sunken_expanse_fallen_leaves")
                        ), false);
                    }
                }
            } else if (biome.is(RAFDimensions.Biomez.THE_PEACH_BLOSSOM_VALE)) {

            }
        }
        if (gameTime % 60 == 0) {

        }
    }
}
