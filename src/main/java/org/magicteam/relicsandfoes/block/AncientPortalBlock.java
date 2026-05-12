package org.magicteam.relicsandfoes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.network.TeleportStatePacket;
import org.mesdag.particlestorm.PSGameClient;
import org.mesdag.particlestorm.particle.ParticleEmitter;

public class AncientPortalBlock extends Block {
    public AncientPortalBlock() {
        super(Properties.of().strength(-1, 3600000.8F));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            PSGameClient.LOADER.addEmitter(new ParticleEmitter(level, pos.getCenter(), RelicsAndFoes.asResource("portal_mist_start")), false);
        } else {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new TeleportStatePacket(TeleportStatePacket.START));
        }
        return InteractionResult.SUCCESS;
    }
}
