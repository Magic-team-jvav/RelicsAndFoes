package org.magicteam.relicsandfoes.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.component.LostSoloMelodyComponent;
import org.magicteam.relicsandfoes.init.RAFDataComponents;
import org.magicteam.relicsandfoes.network.c2s.SelectMusicPacketC2S;
import org.magicteam.relicsandfoes.world.item.LostSoloMelodyItem;

import java.util.Arrays;
import java.util.List;

import static org.magicteam.relicsandfoes.client.RAFSharedValues.available;
import static org.magicteam.relicsandfoes.client.RAFSharedValues.player;

public class SelectMusicGuiLayer implements LayeredDraw.Layer {
    public static final ResourceLocation ID = RelicsAndFoes.asResource("select_music");
    private static final ResourceLocation SPRITE = ResourceLocation.withDefaultNamespace("advancements/title_box");
    public static boolean selecting;
    public static int scrolled;
    public static @Nullable Music selectedMusic;

    private static float currentOffsetY;
    private static float[] currentScale = new float[0];

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (available && player.isUsingItem() && player.getUseItem().getItem() instanceof LostSoloMelodyItem) {
            selecting = true;
            LostSoloMelodyComponent component = player.getUseItem().get(RAFDataComponents.LOST_SOLO_MELODY);
            if (component == null) return;
            int selected = component.limit(component.selected() + scrolled);
            List<Music> musics = component.musics();
            Font font = Minecraft.getInstance().font;
            PoseStack poseStack = guiGraphics.pose();
            int centerX = guiGraphics.guiWidth() / 2;
            int centerY = guiGraphics.guiHeight() / 2;
            int width = 200;
            int height = 16;
            float radius = 5.0F;

            if (currentScale.length != musics.size() + 1) {
                currentScale = new float[musics.size() + 1];
                Arrays.fill(currentScale, 1);
                currentOffsetY = 0;
            }

            int stepOffsetY = 3 + height;
            float targetOffsetY = (1 + selected) * -stepOffsetY;
            currentOffsetY += (targetOffsetY - currentOffsetY) * 0.1F;
            float offsetY = currentOffsetY;

            for (int i = -1; i < musics.size(); i++) {
                Component text;
                if (i == -1) {
                    text = Component.literal("停止播放");
                } else {
                    text = Component.translatable(LostSoloMelodyComponent.getDescriptionId(musics.get(i)));
                }

                int offset = Math.abs(selected - i);
                float targetScale = Mth.clamp((float) Math.sqrt(Math.max(radius * radius - offset * offset, 0)) / radius, 0, 1);
                currentScale[i + 1] += (targetScale - currentScale[i + 1]) * 0.1F;
                float scale = currentScale[i + 1];

                RenderSystem.setShaderColor(1, 1, 1, Mth.clamp(1 - offset * 0.3F, 0, 1));
                RenderSystem.enableBlend();
                poseStack.pushPose();
                poseStack.translate(centerX - width * scale * 0.5F, centerY - height * 0.5F + offsetY, 0);
                poseStack.scale(scale, scale, 1);
                guiGraphics.blitSprite(SPRITE, 0, 0, width, font.lineHeight + height);
                poseStack.popPose();
                poseStack.pushPose();
                poseStack.translate(centerX, centerY + offsetY, 0);
                guiGraphics.drawCenteredString(font, text, 0, 0, 0xFFFFFFFF);
                poseStack.popPose();
                RenderSystem.disableBlend();
                RenderSystem.setShaderColor(1, 1, 1, 1);
                offsetY += stepOffsetY;
            }
        } else if (selecting) {
            selecting = false;
            if (available && player.getMainHandItem().getItem() instanceof LostSoloMelodyItem) {
                LostSoloMelodyComponent component = player.getMainHandItem().get(RAFDataComponents.LOST_SOLO_MELODY);
                if (component != null) {
                    int selected = component.selected() + scrolled;
                    Music music = component.getMusic(selected);
                    if (music == null && selectedMusic != null) {
                        Minecraft.getInstance().getMusicManager().stopPlaying(selectedMusic);
                    }
                    selectedMusic = music;
                    PacketDistributor.sendToServer(new SelectMusicPacketC2S(selected));
                    Component playing;
                    if (selectedMusic == null) {
                        playing = Component.translatable("tooltip.lost_solo_melody.none");
                    } else {
                        playing = Component.translatable(LostSoloMelodyComponent.getDescriptionId(selectedMusic));
                    }
                    Minecraft.getInstance().gui.setNowPlaying(playing);
                }
            }
            scrolled = 0;
        }
    }
}
