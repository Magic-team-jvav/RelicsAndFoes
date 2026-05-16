package org.magicteam.relicsandfoes.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.component.LostSoloMelodyComponent;
import org.magicteam.relicsandfoes.init.RAFDataComponents;
import org.magicteam.relicsandfoes.world.item.LostSoloMelodyItem;

public record SelectMusicPacketC2S(int selected) implements CustomPacketPayload {
    public static final Type<SelectMusicPacketC2S> TYPE = new Type<>(RelicsAndFoes.asResource("select_music"));
    public static final StreamCodec<ByteBuf, SelectMusicPacketC2S> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(SelectMusicPacketC2S::new, SelectMusicPacketC2S::selected);

    @Override
    public Type<SelectMusicPacketC2S> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() instanceof LostSoloMelodyItem) {
                    LostSoloMelodyComponent component = stack.get(RAFDataComponents.LOST_SOLO_MELODY);
                    if (component != null) {
                        stack.set(RAFDataComponents.LOST_SOLO_MELODY, component.select(selected));
                    }
                }
            }
        });
    }
}
