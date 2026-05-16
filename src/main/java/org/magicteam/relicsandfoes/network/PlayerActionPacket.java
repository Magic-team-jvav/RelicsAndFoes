package org.magicteam.relicsandfoes.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.client.screen.ReadyToTeleportScreen;
import org.magicteam.relicsandfoes.init.RAFDimensions;
import org.mesdag.particlestorm.data.molang.MolangExp;
import org.mesdag.particlestorm.network.EmitterCreationPacketS2C;

public record PlayerActionPacket(byte state) implements CustomPacketPayload {
    public static final Type<PlayerActionPacket> TYPE = new Type<>(RelicsAndFoes.asResource("player_action"));
    public static final StreamCodec<ByteBuf, PlayerActionPacket> STREAM_CODEC = ByteBufCodecs.BYTE.map(PlayerActionPacket::new, PlayerActionPacket::state);
    private static final DimensionTransition.PostDimensionTransition BURST_PARTICLE = entity -> {
        if (entity instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new EmitterCreationPacketS2C(
                    RelicsAndFoes.asResource("portal_mist_end"),
                    player.position().toVector3f(),
                    MolangExp.EMPTY,
                    player.getId()
            ));
        }
    };

    public static final byte TELEPORT_START = 1;
    public static final byte TELEPORT_END = 2;

    @Override
    public Type<PlayerActionPacket> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                if (state == TELEPORT_START) {
                    ReadyToTeleportScreen.setScreen();
                }
            } else if (context.player() instanceof ServerPlayer player) {
                if (state == TELEPORT_END) {
                    if (player.level().dimension() == Level.OVERWORLD) {
                        ServerLevel level = player.server.getLevel(RAFDimensions.LEVEL);
                        if (level != null) {
                            player.changeDimension(new DimensionTransition(level, new Vec3(0.5, 64, 0.5), Vec3.ZERO, 0, 0, false, BURST_PARTICLE));
                        }
                    } else if (player.level().dimension() == RAFDimensions.LEVEL) {
                        player.changeDimension(player.findRespawnPositionAndUseSpawnBlock(true, BURST_PARTICLE));
                    }
                }
            }
        });
    }
}
