package org.magicteam.relicsandfoes.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.util.function.BooleanSupplier;

public class AfterTeleportScreen extends ReceivingLevelScreen {
    private final BooleanSupplier levelReceived;
    private int tickCount;

    public AfterTeleportScreen(BooleanSupplier levelReceived, Reason reason) {
        super(levelReceived, reason);
        this.levelReceived = levelReceived;
        tickCount = 40;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, width, height, FastColor.ARGB32.color((int) (Mth.clamp(tickCount / 40.0F, 0, 1) * 255), 0xFFFFFF));
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
}
