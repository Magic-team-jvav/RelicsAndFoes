package org.magicteam.relicsandfoes.mixed;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.ResourceLocation;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.world.item.RAFCreativeModeTab;

import java.util.List;

public interface RAFCreativeModeInventoryScreen {
    Variables raf$getVariables();

    void raf$refreshCurrentTabContents(RAFCreativeModeTab.SideTab sideTab);

    class Variables {
        public static RAFCreativeModeTab.SideTab selectedSideTab = RAFCreativeModeTab.SideTab.RELIC_LAND;

        public final List<SideTabButton> buttons;

        public Variables(RAFCreativeModeInventoryScreen screen) {
            this.buttons = Lists.newArrayListWithExpectedSize(RAFCreativeModeTab.SideTab.VALUES.length);
            for (RAFCreativeModeTab.SideTab tab : RAFCreativeModeTab.SideTab.VALUES) {
                SideTabButton sideTabButton = new SideTabButton(screen, tab);

                buttons.add(sideTabButton);
            }
        }
    }

    class SideTabButton extends ImageButton {
        private static final WidgetSprites SPRITES = new WidgetSprites(
                RelicsAndFoes.asResource("side_tab"),
                RelicsAndFoes.asResource("side_tab_highlighted")
        );
        protected boolean clicked;
        public final RAFCreativeModeTab.SideTab tab;

        public SideTabButton(RAFCreativeModeInventoryScreen screen, RAFCreativeModeTab.SideTab tab) {
            super(0, 0, 16, 16, SPRITES, button -> onPress((SideTabButton) button, screen, tab), tab.getTranslatedName());
            this.tab = tab;
            if (Variables.selectedSideTab == tab) {
                this.clicked = true;
            }
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            ResourceLocation sprite = sprites.get(clicked, isHoveredOrFocused());
            guiGraphics.blitSprite(sprite, getX(), getY(), getWidth(), getHeight());
            guiGraphics.renderFakeItem(tab.getIcon(), getX(), getY());
            if (isHovered()) {
                guiGraphics.renderTooltip(Minecraft.getInstance().font, tab.getTranslatedName(), mouseX, mouseY);
            }
        }

        private static void onPress(SideTabButton thiz, RAFCreativeModeInventoryScreen screen, RAFCreativeModeTab.SideTab tab) {
            for (SideTabButton button : screen.raf$getVariables().buttons) {
                if (button != thiz) {
                    button.clicked = false;
                }
            }
            thiz.clicked = true;
            screen.raf$refreshCurrentTabContents(Variables.selectedSideTab = tab);
        }

        public void setupPos(int leftPos, int topPos) {
            setPosition(leftPos - getWidth(), topPos + (getHeight() + 4) * tab.ordinal());
        }
    }
}
