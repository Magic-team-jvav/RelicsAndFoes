package org.magicteam.relicsandfoes.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.magicteam.relicsandfoes.RelicsAndFoes;

import java.util.function.BooleanSupplier;

public class AfterTeleportScreen extends ReceivingLevelScreen {
    private static final ResourceLocation[] MIST_TEXTURES = {
        RelicsAndFoes.asResource("textures/particle/new_mist_0.png"),
        RelicsAndFoes.asResource("textures/particle/new_mist_1.png"),
        RelicsAndFoes.asResource("textures/particle/new_mist_2.png"),
    };

    private final BooleanSupplier levelReceived;
    private int tickCount;

    public AfterTeleportScreen(BooleanSupplier levelReceived, Reason reason) {
        super(levelReceived, reason);
        this.levelReceived = levelReceived;
        tickCount = 40;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        float alpha = Mth.clamp(tickCount / 40.0F, 0, 1);
        if (alpha > 0) {
            guiGraphics.fill(0, 0, width, height, FastColor.ARGB32.color((int) (alpha * 255), 0xFFFFFF));
            renderClouds(guiGraphics, tickCount + partialTick, alpha, width, height);
        }
    }

    @Override
    public void tick() {
        if (levelReceived.getAsBoolean()) {
            --tickCount;
            if (tickCount < 0) {
                onClose();
            }
        }
    }

    public static void renderClouds(GuiGraphics guiGraphics, float time, float alpha, int width, int height) {
        RenderSystem.enableBlend();
        for (int i = 0; i < MIST_TEXTURES.length; i++) {
            float speed = 0.5f + i * 0.2f;
            float scale = 1.2f + i * 0.2f;
            float amplitude = 0.06f + i * 0.03f;
            float layerAlpha = alpha * (0.28f + i * 0.1f);
            float phase = i * Mth.PI / 3.0f;

            int texW = (int) (width * scale);
            int texH = (int) (height * scale);

            float dx = Mth.sin(time * 0.018f * speed + phase) * amplitude * width;
            float dy = Mth.cos(time * 0.014f * speed + phase + 0.7f) * amplitude * height;

            int x = (int) (dx - (texW - width) / 2f);
            int y = (int) (dy - (texH - height) / 2f);

            RenderSystem.setShaderColor(1, 1, 1, layerAlpha);
            guiGraphics.blit(MIST_TEXTURES[i], x, y, 0, 0, texW, texH, texW, texH);
        }
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }
}
