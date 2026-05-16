package org.magicteam.relicsandfoes.world.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.network.PlayerActionPacket;
import org.mesdag.particlestorm.PSGameClient;
import org.mesdag.particlestorm.particle.ParticleEmitter;

public class PortableAncientRelicTeleporterItem extends RAFTooltipItem {
    public PortableAncientRelicTeleporterItem(ResourceLocation id) {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), id, 1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide) {
            PSGameClient.LOADER.addEmitter(new ParticleEmitter(level, player.blockPosition().getCenter(), RelicsAndFoes.asResource("portal_mist_start")));
        } else {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new PlayerActionPacket(PlayerActionPacket.TELEPORT_START));
        }
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
