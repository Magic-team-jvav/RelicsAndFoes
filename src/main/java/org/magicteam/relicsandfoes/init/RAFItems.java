package org.magicteam.relicsandfoes.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.world.item.LostSoloMelodyItem;
import org.magicteam.relicsandfoes.world.item.PortableAncientRelicTeleporterItem;

public final class RAFItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RelicsAndFoes.MODID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(RelicsAndFoes.MODID);

    public static final DeferredItem<PortableAncientRelicTeleporterItem> PORTABLE_ANCIENT_RELIC_TELEPORTER = ITEMS.register("portable_ancient_relic_teleporter", PortableAncientRelicTeleporterItem::new);

    public static final DeferredItem<LostSoloMelodyItem> LOST_SOLO_MELODY_ANGEL_MOVEMENT = ITEMS.register("lost_solo_melody_angel_movement", () -> new LostSoloMelodyItem(RAFMusics.BOSS_ANGEL, RAFMusics.BIOME_ANGEL));
    public static final DeferredItem<LostSoloMelodyItem> LOST_SOLO_MELODY_CONDUCTOR_MOVEMENT = ITEMS.register("lost_solo_melody_conductor_movement", () -> new LostSoloMelodyItem(RAFMusics.BOSS_CONDUCTOR, RAFMusics.BIOME_CONDUCTOR));
    public static final DeferredItem<LostSoloMelodyItem> LOST_SOLO_MELODY_KYLIN_MOVEMENT = ITEMS.register("lost_solo_melody_kylin_movement", () -> new LostSoloMelodyItem(RAFMusics.BOSS_KYLIN, RAFMusics.BIOME_KYLIN));
    public static final DeferredItem<LostSoloMelodyItem> LOST_SOLO_MELODY_MECHANICAL_MOVEMENT = ITEMS.register("lost_solo_melody_mechanical_movement", () -> new LostSoloMelodyItem(RAFMusics.BOSS_MECHANICAL_0, RAFMusics.BOSS_MECHANICAL_1, RAFMusics.BIOME_MECHANICAL));
    public static final DeferredItem<LostSoloMelodyItem> LOST_SOLO_MELODY_PROTOTYPE_MOVEMENT = ITEMS.register("lost_solo_melody_prototype_movement", () -> new LostSoloMelodyItem(RAFMusics.BOSS_PROTOTYPE_1, RAFMusics.BOSS_PROTOTYPE_4, RAFMusics.BIOME_PROTOTYPE, RAFMusics.BIOME_PROTOTYPE_WIND));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
    }
}
