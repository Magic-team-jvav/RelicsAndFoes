package org.magicteam.relicsandfoes;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.magicteam.relicsandfoes.network.TeleportStatePacket;

@EventBusSubscriber(modid = RelicsAndFoes.MODID)
public final class RAFEvents {
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playBidirectional(TeleportStatePacket.TYPE, TeleportStatePacket.STREAM_CODEC, TeleportStatePacket::handle)
        ;
    }
}
