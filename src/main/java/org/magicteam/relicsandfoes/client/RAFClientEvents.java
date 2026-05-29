package org.magicteam.relicsandfoes.client;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.client.gui.SelectMusicGuiLayer;
import org.magicteam.relicsandfoes.client.screen.AfterTeleportScreen;
import org.magicteam.relicsandfoes.init.RAFBlocks;
import org.magicteam.relicsandfoes.init.RAFDimensions;
import org.magicteam.relicsandfoes.init.RAFMusics;
import org.magicteam.relicsandfoes.world.block.AncientRelicTeleporterBlock;
import org.magicteam.relicsandfoes.world.item.LostSoloMelodyItem;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import static org.magicteam.relicsandfoes.client.RAFSharedValues.*;

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

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, getter, pos, index) -> getter != null && pos != null ? BiomeColors.getAverageGrassColor(getter, state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos) : GrassColor.getDefaultColor(),
                RAFBlocks.ANCIENT_TALL_WILDGRASS.get()
        );
        event.register((state, getter, pos, index) -> getter != null && pos != null ? BiomeColors.getAverageGrassColor(getter, pos) : GrassColor.getDefaultColor(),
                RAFBlocks.ANCIENT_WILDGRASS.get()
        );
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, SelectMusicGuiLayer.ID, new SelectMusicGuiLayer());
    }

    @SubscribeEvent
    public static void renderGuiLayer$Pre(RenderGuiLayerEvent.Pre event) {
        if (SelectMusicGuiLayer.selecting && VanillaGuiLayers.CROSSHAIR.equals(event.getName())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void clientPlayerNetwork$Clone(ClientPlayerNetworkEvent.Clone event) {
        RAFSharedValues.update(event.getPlayer(), true);
    }

    @SubscribeEvent
    public static void clientPlayerNetwork$LoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        RAFSharedValues.reset();
    }

    @SubscribeEvent
    public static void clientTick$Pre(ClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        RAFSharedValues.update(minecraft.player, false);
        if (available) {
            if (!minecraft.isPaused()) {
                RelicsAndFoesClient.biomeParticles();
                BiomeFog biomeFog = BiomeFog.get();
                if (biomeFog != null) {
                    if (BiomeFog.targetFog == null) {
                        BiomeFog.prevNearPlaneNone = biomeFog.nearPlaneNone();
                        BiomeFog.prevNearPlaneWater = biomeFog.nearPlaneWater();
                        BiomeFog.prevFarPlaneWater = biomeFog.farPlaneWater();
                        BiomeFog.targetFog = biomeFog;
                        BiomeFog.paramBlend = 1;
                    } else if (!biomeFog.equals(BiomeFog.targetFog)) {
                        BiomeFog.prevNearPlaneNone = Mth.lerp(BiomeFog.paramBlend, BiomeFog.prevNearPlaneNone, BiomeFog.targetFog.nearPlaneNone());
                        BiomeFog.prevNearPlaneWater = Mth.lerp(BiomeFog.paramBlend, BiomeFog.prevNearPlaneWater, BiomeFog.targetFog.nearPlaneWater());
                        BiomeFog.prevFarPlaneWater = Mth.lerp(BiomeFog.paramBlend, BiomeFog.prevFarPlaneWater, BiomeFog.targetFog.farPlaneWater());
                        BiomeFog.targetFog = biomeFog;
                        BiomeFog.paramBlend = 0;
                    }
                    BiomeFog.fogBlend = Math.min(1, BiomeFog.fogBlend + 0.05F);
                    BiomeFog.paramBlend = Math.min(1, BiomeFog.paramBlend + 0.05F);
                } else {
                    BiomeFog.fogBlend = Math.max(0, BiomeFog.fogBlend - 0.05F);
                }
            }
            SelectMusicGuiLayer.selecting = player.isUsingItem() && player.getUseItem().getItem() instanceof LostSoloMelodyItem;
        }
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
        if (BiomeFog.fogBlend > 0 && BiomeFog.targetFog != null) {
            FogType type = event.getType();
            float currentNear = event.getNearPlaneDistance();
            float currentFar = event.getFarPlaneDistance();
            event.setCanceled(true);
            event.setFogShape(FogShape.SPHERE);
            if (type == FogType.NONE) {
                float nearTarget = BiomeFog.paramBlend >= 1 ? BiomeFog.targetFog.nearPlaneNone() : Mth.lerp(BiomeFog.paramBlend, BiomeFog.prevNearPlaneNone, BiomeFog.targetFog.nearPlaneNone());
                event.setNearPlaneDistance(Mth.lerp(BiomeFog.fogBlend, currentNear, nearTarget));
            } else if (type == FogType.WATER) {
                float nearTarget = BiomeFog.paramBlend >= 1 ? BiomeFog.targetFog.nearPlaneWater() : Mth.lerp(BiomeFog.paramBlend, BiomeFog.prevNearPlaneWater, BiomeFog.targetFog.nearPlaneWater());
                float farTarget = BiomeFog.paramBlend >= 1 ? BiomeFog.targetFog.farPlaneWater() : Mth.lerp(BiomeFog.paramBlend, BiomeFog.prevFarPlaneWater, BiomeFog.targetFog.farPlaneWater());
                event.setNearPlaneDistance(Mth.lerp(BiomeFog.fogBlend, currentNear, nearTarget));
                event.setFarPlaneDistance(Mth.lerp(BiomeFog.fogBlend, currentFar, farTarget));
            }
        }
    }

    @SubscribeEvent
    public static void selectMusic(SelectMusicEvent event) {
        if (SelectMusicGuiLayer.selectedMusic != null) {
            event.overrideMusic(SelectMusicGuiLayer.selectedMusic);
        } else if (available) {
            if (inTheSunkenExpanse) {
                if (inCrossRealmAncientRuins && !isPrototypeDefeated) {
                    event.overrideMusic(RAFMusics.BIOME_PROTOTYPE_WIND);
                } else {
                    event.overrideMusic(RAFMusics.BIOME_PROTOTYPE);
                }
            } else if (inTheThornyDreadlands) {
                event.overrideMusic(RAFMusics.BIOME_ANGEL);
            } else if (inTheSeaOfFallingStars) {
                event.overrideMusic(RAFMusics.BIOME_CONDUCTOR);
            } else if (inThePeachOfBlossomVale) {
                event.overrideMusic(RAFMusics.BIOME_KYLIN);
            }
        }
    }

    @SubscribeEvent
    public static void input$MouseScrolling(InputEvent.MouseScrollingEvent event) {
        if (SelectMusicGuiLayer.selecting) {
            SelectMusicGuiLayer.scrolled -= Mth.sign(event.getScrollDeltaY());
            event.setCanceled(true);
        }
    }
}
