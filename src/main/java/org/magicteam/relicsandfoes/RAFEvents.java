package org.magicteam.relicsandfoes;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.magicteam.relicsandfoes.network.TeleportStatePacket;
import org.magicteam.relicsandfoes.util.LootTableReplacer;

@EventBusSubscriber(modid = RelicsAndFoes.MODID)
public final class RAFEvents {
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playBidirectional(TeleportStatePacket.TYPE, TeleportStatePacket.STREAM_CODEC, TeleportStatePacket::handle)
        ;
    }

    @SubscribeEvent
    public static void playerInteract$RightClickEmpty(PlayerInteractEvent.RightClickItem event) {
        if (FMLEnvironment.production ||
                event.getLevel().isClientSide ||
                event.getHand() == InteractionHand.OFF_HAND
        ) {
            return;
        }
        if (event.getItemStack().is(Items.STICK)) {
            LootTableReplacer.replace((ServerLevel) event.getLevel(), new BlockPos(85, -45, 33), new BlockPos(109, -43, 12), new BlockPos(110, -46, -37), new BlockPos(116, -46, 26), new BlockPos(124, -45, -48), new BlockPos(124, -44, -39), new BlockPos(132, -46, 15), new BlockPos(137, -45, 25), new BlockPos(154, -44, -42), new BlockPos(154, -44, -17), new BlockPos(156, -41, -84), new BlockPos(158, -44, -42), new BlockPos(159, -44, 40), new BlockPos(162, -43, -81), new BlockPos(162, -28, -58), new BlockPos(165, -34, -75), new BlockPos(166, -38, -61), new BlockPos(168, -34, -35), new BlockPos(168, -34, -18), new BlockPos(175, -18, -23), new BlockPos(176, -44, -14), new BlockPos(177, -21, -63), new BlockPos(180, -38, -10), new BlockPos(185, -45, 24), new BlockPos(186, -25, -69), new BlockPos(187, -8, -47), new BlockPos(188, -28, -77), new BlockPos(188, -16, -23), new BlockPos(190, -44, -86), new BlockPos(192, -46, -12), new BlockPos(195, -44, -66), new BlockPos(195, -38, -6), new BlockPos(196, -42, 40), new BlockPos(196, -34, -79), new BlockPos(197, -44, -66), new BlockPos(198, -38, -14), new BlockPos(201, -46, -15), new BlockPos(201, -46, -14), new BlockPos(201, -41, 44), new BlockPos(202, -45, -101), new BlockPos(202, -44, -6), new BlockPos(206, -38, -74), new BlockPos(207, -45, -3), new BlockPos(218, -44, -1), new BlockPos(218, -38, -82), new BlockPos(218, -28, -44), new BlockPos(220, -30, -68), new BlockPos(221, -26, -19), new BlockPos(223, -17, -28), new BlockPos(224, -45, -8), new BlockPos(224, -34, -20), new BlockPos(225, -44, -8), new BlockPos(226, -44, -8), new BlockPos(229, -27, -69), new BlockPos(230, -36, 28), new BlockPos(248, -42, -1), new BlockPos(248, -15, 30), new BlockPos(251, -42, 8), new BlockPos(252, -38, 8), new BlockPos(265, -44, -17), new BlockPos(266, -43, -44), new BlockPos(268, -23, 23), new BlockPos(277, -45, -33), new BlockPos(277, -44, -78), new BlockPos(287, -37, 3), new BlockPos(298, -33, 5), new BlockPos(305, -44, -44), new BlockPos(312, -44, 32));
        }
    }
}
