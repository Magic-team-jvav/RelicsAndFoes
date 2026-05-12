package org.magicteam.relicsandfoes.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import org.magicteam.relicsandfoes.network.TeleportStatePacket;

public class ReadyToTeleportScreen extends Screen {
    private int tickCount;
    private boolean hasSend;

    public ReadyToTeleportScreen() {
        super(Component.empty());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, width, height, FastColor.ARGB32.color((int) Mth.clamp(tickCount / 80.0, 0, 1) * 255, 0xFFFFFF));
    }

    @Override
    public void tick() {
        if (hasSend) return;
        ++tickCount;
        if (tickCount > 100) {
            hasSend = true;
            PacketDistributor.sendToServer(new TeleportStatePacket(TeleportStatePacket.END));
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static void setScreen() {
        Minecraft.getInstance().setScreen(new ReadyToTeleportScreen());
    }
}
