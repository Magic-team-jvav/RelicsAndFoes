package org.magicteam.relicsandfoes.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.Music;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.magicteam.relicsandfoes.component.LostSoloMelodyComponent;
import org.magicteam.relicsandfoes.init.RAFDataComponents;

import java.util.Arrays;
import java.util.List;

public class LostSoloMelodyItem extends Item {
    public LostSoloMelodyItem(Music... musics) {
        super(new Properties().stacksTo(1).component(
                RAFDataComponents.LOST_SOLO_MELODY,
                new LostSoloMelodyComponent(Arrays.asList(musics), -1)
        ).rarity(Rarity.RARE));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return ItemUtils.startUsingInstantly(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        LostSoloMelodyComponent component = stack.get(RAFDataComponents.LOST_SOLO_MELODY);
        if (component != null) {
            component.appendTooltips(tooltipComponents);
        }
    }
}
