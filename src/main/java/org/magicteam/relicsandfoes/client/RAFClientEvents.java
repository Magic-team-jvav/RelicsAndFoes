package org.magicteam.relicsandfoes.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.DimensionTransitionScreenManager;
import net.neoforged.neoforge.client.event.RegisterDimensionTransitionScreenEvent;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.ModDimensions;

@EventBusSubscriber(modid = RelicsAndFoes.MODID, value = Dist.CLIENT)
public final class RAFClientEvents {
    @SubscribeEvent
    public static void registerDimensionTransitionScreen(RegisterDimensionTransitionScreenEvent event) {
        DimensionTransitionScreenManager.ReceivingLevelScreenFactory factory = (levelReceived, reason) -> new ReceivingLevelScreen(levelReceived, reason) {
            private int tickCount = 80;

            @Override
            public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                guiGraphics.fill(0, 0, width, height, FastColor.ARGB32.color((int) Mth.clamp(tickCount / 80.0F, 0, 1) * 255, 0xFFFFFF));
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
        };
        event.registerIncomingEffect(ModDimensions.LEVEL, factory);
        event.registerOutgoingEffect(ModDimensions.LEVEL, factory);
    }
}
