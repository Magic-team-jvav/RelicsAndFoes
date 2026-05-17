package org.magicteam.relicsandfoes.mixin.net.minecraft.client.gui.screens.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.CreativeTabsScreenPage;
import org.magicteam.relicsandfoes.mixed.RAFCreativeModeInventoryScreen;
import org.magicteam.relicsandfoes.world.item.RAFCreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> implements RAFCreativeModeInventoryScreen {
    @Shadow
    private static CreativeModeTab selectedTab;

    @Shadow
    protected abstract void refreshCurrentTabContents(Collection<ItemStack> items);

    @Shadow
    private CreativeTabsScreenPage currentPage;
    @Unique
    private final Variables raf$variables = new Variables(this);

    public CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public Variables raf$getVariables() {
        return raf$variables;
    }

    @Override
    public void raf$refreshCurrentTabContents(RAFCreativeModeTab.SideTab sideTab) {
        if (selectedTab instanceof RAFCreativeModeTab tab) {
            tab.setDisplayItems(sideTab);
            refreshCurrentTabContents(tab.getDisplayItems());
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        for (SideTabButton button : raf$getVariables().buttons) {
            button.setupPos(leftPos, topPos);
            addRenderableWidget(button);
        }
    }

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void afterSelect(CallbackInfo ci, @Local(ordinal = 1) CreativeModeTab tabBefore) {
        boolean visible = selectedTab instanceof RAFCreativeModeTab;
        for (SideTabButton button : raf$getVariables().buttons) {
            button.visible = visible;
        }
        if (visible && tabBefore != selectedTab) {
            raf$refreshCurrentTabContents(Variables.selectedSideTab);
        }
    }

    @WrapOperation(method = "renderLabels", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;getDisplayName()Lnet/minecraft/network/chat/Component;"))
    private Component modifyLabel(CreativeModeTab instance, Operation<Component> original) {
        if (instance instanceof RAFCreativeModeTab) {
            return Variables.selectedSideTab.getTranslatedName();
        }
        return original.call(instance);
    }
}
