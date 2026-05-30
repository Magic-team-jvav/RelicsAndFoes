package org.magicteam.relicsandfoes.client;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
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
                BiomeFog.blend();
            }
            SelectMusicGuiLayer.selecting = player.isUsingItem() && player.getUseItem().getItem() instanceof LostSoloMelodyItem;
        }
    }

    @SubscribeEvent
    public static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(RAFDimensions.ID, new DimensionSpecialEffects.OverworldEffects() {
            private static final float r = 1;
            private static final float g = (float) 0xB8 / 0xFF;
            private static final float b = (float) 0x75 / 0xFF;

            @Override
            public float @Nullable [] getSunriseColor(float timeOfDay, float partialTicks) {
                if (inTheSunkenExpanse || inThePeachOfBlossomVale) {
                    float ratio = Mth.cos(timeOfDay * Mth.TWO_PI);
                    if (ratio >= -0.4F && ratio <= 0.4F) {
                        float step = ratio / 0.4F * 0.5F + 0.5F;
                        float v = 0.3F;
                        sunriseCol[0] = step * v + r - v;
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
        if (BiomeFog.fogBlend > 0 && BiomeFog.targetFog != null && event.getMode() == FogRenderer.FogMode.FOG_TERRAIN) {
            float partialFog = BiomeFog.fogBlend + (float) event.getPartialTick();
            float partialParam = BiomeFog.paramBlend + (float) event.getPartialTick();
            FogType type = event.getType();
            float currentNear = event.getNearPlaneDistance();
            float currentFar = event.getFarPlaneDistance();
            event.setCanceled(true);
            event.setFogShape(FogShape.SPHERE);
            if (type == FogType.NONE) {
                float nearTarget = Mth.clampedLerp(BiomeFog.prevNearPlaneNone, BiomeFog.targetFog.nearPlaneNone(), partialParam);
                float farTarget = Mth.clampedLerp(BiomeFog.prevFarPlaneNone, BiomeFog.targetFog.farPlaneNone(), partialParam);
                event.setNearPlaneDistance(currentFar * nearTarget);
                event.setFarPlaneDistance(Mth.clampedLerp(partialFog, currentFar, currentFar * farTarget));
            } else if (type == FogType.WATER) {
                float nearTarget = Mth.clampedLerp(BiomeFog.prevNearPlaneWater, BiomeFog.targetFog.nearPlaneWater(), partialParam);
                float farTarget = Mth.clampedLerp(BiomeFog.prevFarPlaneWater, BiomeFog.targetFog.farPlaneWater(), partialParam);
                event.setNearPlaneDistance(Mth.clampedLerp(partialFog, currentNear, nearTarget));
                event.setFarPlaneDistance(Mth.clampedLerp(partialFog, currentFar, farTarget));
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
