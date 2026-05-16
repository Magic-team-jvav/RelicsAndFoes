package org.magicteam.relicsandfoes.init;

import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;

public final class RAFMusics {
    public static final Music BOSS_KYLIN = create(RAFSounds.MUSIC_BOSS_KYLIN);
    public static final Music BIOME_KYLIN = create(RAFSounds.MUSIC_BIOME_KYLIN);
    public static final Music BOSS_ANGEL = create(RAFSounds.MUSIC_BOSS_ANGEL);
    public static final Music BIOME_ANGEL = create(RAFSounds.MUSIC_BIOME_ANGEL);
    public static final Music BOSS_CONDUCTOR = create(RAFSounds.MUSIC_BOSS_CONDUCTOR);
    public static final Music BIOME_CONDUCTOR = create(RAFSounds.MUSIC_BIOME_CONDUCTOR);
    public static final Music BOSS_PROTOTYPE_1 = create(RAFSounds.MUSIC_BOSS_PROTOTYPE_1);
    public static final Music BOSS_PROTOTYPE_4 = create(RAFSounds.MUSIC_BOSS_PROTOTYPE_4);
    public static final Music BIOME_PROTOTYPE = create(RAFSounds.MUSIC_BIOME_PROTOTYPE);
    public static final Music BIOME_PROTOTYPE_WIND = create(RAFSounds.MUSIC_BIOME_PROTOTYPE_WIND);
    public static final Music BOSS_MECHANICAL_0 = create(RAFSounds.MUSIC_BOSS_MECHANICAL_0);
    public static final Music BOSS_MECHANICAL_1 = create(RAFSounds.MUSIC_BOSS_MECHANICAL_1);
    public static final Music BIOME_MECHANICAL = create(RAFSounds.MUSIC_BIOME_MECHANICAL);

    private static Music create(Holder<SoundEvent> holder) {
        return new Music(holder, 0, 0, true);
    }
}
