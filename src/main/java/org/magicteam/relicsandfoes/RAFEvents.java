package org.magicteam.relicsandfoes;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.magicteam.relicsandfoes.network.PlayerActionPacket;
import org.magicteam.relicsandfoes.network.c2s.SelectMusicPacketC2S;

@EventBusSubscriber(modid = RelicsAndFoes.MODID)
public final class RAFEvents {
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playBidirectional(PlayerActionPacket.TYPE, PlayerActionPacket.STREAM_CODEC, PlayerActionPacket::handle)
                .playToServer(SelectMusicPacketC2S.TYPE, SelectMusicPacketC2S.STREAM_CODEC, SelectMusicPacketC2S::handle)
        ;
    }
}
