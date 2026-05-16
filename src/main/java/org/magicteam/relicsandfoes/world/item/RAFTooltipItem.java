package org.magicteam.relicsandfoes.world.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.Arrays;
import java.util.List;

public class RAFTooltipItem extends Item {
    private final List<Component> tooltips;

    public RAFTooltipItem(Properties properties, Component... tooltips) {
        super(properties);
        this.tooltips = Arrays.asList(tooltips);
    }

    public RAFTooltipItem(Properties properties, ResourceLocation id, int lines) {
        super(properties);
        ImmutableList.Builder<Component> builder = ImmutableList.builder();
        for (int i = 0; i < lines; i++) {
            builder.add(Component.translatable("tooltip.item." + id.getNamespace() + "." + id.getPath() + "." + i).withStyle(ChatFormatting.GRAY));
        }
        this.tooltips = builder.build();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.addAll(tooltips);
    }
}
