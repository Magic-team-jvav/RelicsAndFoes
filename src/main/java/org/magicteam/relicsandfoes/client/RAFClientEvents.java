package org.magicteam.relicsandfoes.client;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.client.screen.AfterTeleportScreen;
import org.magicteam.relicsandfoes.init.RAFBlocks;
import org.magicteam.relicsandfoes.init.RAFDimensions;
import org.magicteam.relicsandfoes.init.RAFMusics;
import org.magicteam.relicsandfoes.world.block.crossrealmancientruins.AncientRelicTeleporterBlock;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@EventBusSubscriber(modid = RelicsAndFoes.MODID, value = Dist.CLIENT)
public final class RAFClientEvents {
    @SubscribeEvent
    public static void registerDimensionTransitionScreen(RegisterDimensionTransitionScreenEvent event) {
        event.registerIncomingEffect(RAFDimensions.LEVEL, AfterTeleportScreen::new);
        event.registerOutgoingEffect(RAFDimensions.LEVEL, AfterTeleportScreen::new);
    }

    @SubscribeEvent
    public static void entityRenderers$RegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(RAFBlocks.ANCIENT_RELIC_TELEPORTER_ENTITY.get(), context -> new GeoBlockRenderer<>(RAFBlocks.ANCIENT_RELIC_TELEPORTER_ENTITY.get()) {
            @Override
            public RenderType getRenderType(AncientRelicTeleporterBlock.BEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
                return RenderType.entityTranslucent(texture);
            }
        });
    }

    static LocalPlayer player;
    static boolean inRelicLand;
    static Holder<Biome> biome;
    static boolean inTheSunkenExpanse;

    @SubscribeEvent
    public static void clientPlayerNetwork$LoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        inRelicLand = false;
        biome = null;
        inTheSunkenExpanse = false;
    }

    @SubscribeEvent
    public static void clientTick$Pre(ClientTickEvent.Pre event) {
        player = Minecraft.getInstance().player;
        if (player == null) return;
        ClientLevel level = player.clientLevel;
        inRelicLand = level.dimension() == RAFDimensions.LEVEL;
        biome = level.getBiome(player.blockPosition());
        inTheSunkenExpanse = biome.is(RAFDimensions.Biomez.THE_SUNKEN_EXPANSE);
        RelicsAndFoesClient.biomeParticles();
    }

    @SubscribeEvent
    public static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(RAFDimensions.ID, new DimensionSpecialEffects.OverworldEffects() {
            private static final float g = (float) 0xB8 / 0xFF;
            private static final float b = (float) 0x75 / 0xFF;

            @Override
            public float @Nullable [] getSunriseColor(float timeOfDay, float partialTicks) {
                if (inTheSunkenExpanse) {
                    float ratio = Mth.cos(timeOfDay * Mth.TWO_PI);
                    if (ratio >= -0.4F && ratio <= 0.4F) {
                        float step = ratio / 0.4F * 0.5F + 0.5F;
                        float v = 0.3F;
                        sunriseCol[0] = step * v + 1 - v;
                        sunriseCol[1] = step * v + g - v;
                        sunriseCol[2] = step * v + b - v;
                        sunriseCol[3] = Mth.square(1.0F - (1.0F - Mth.sin(step * Mth.PI)) * 0.99F);
                        return sunriseCol;
                    }
                }
                return super.getSunriseColor(timeOfDay, partialTicks);
            }

            @Override
            public void adjustLightmapColors(ClientLevel level, float partialTicks, float skyDarken, float blockLightRedFlicker, float skyLight, int pixelX, int pixelY, Vector3f colors) {
                if (skyDarken > 0) {
                    colors.add(skyDarken, skyDarken, skyDarken);
                }
            }
        });
    }

    @SubscribeEvent
    public static void viewport$RenderFog(ViewportEvent.RenderFog event) {
        if (inTheSunkenExpanse) {
            FogType type = event.getType();
            if (type == FogType.NONE) {
                event.setFogShape(FogShape.SPHERE);
                event.setNearPlaneDistance(0);
                event.setFarPlaneDistance(168);
                event.setCanceled(true);
            } else if (type == FogType.WATER) {
                event.setFogShape(FogShape.SPHERE);
                event.setNearPlaneDistance(6);
                event.setFarPlaneDistance(15);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void viewport$ComputeFogColor(ViewportEvent.ComputeFogColor event) {

    }

    @SubscribeEvent
    public static void selectMusic(SelectMusicEvent event) {
        if (inTheSunkenExpanse) {
            event.overrideMusic(RAFMusics.THE_SUNKEN_EXPANSE);
        }
    }
}
