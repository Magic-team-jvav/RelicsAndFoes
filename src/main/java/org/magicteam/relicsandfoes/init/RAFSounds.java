package org.magicteam.relicsandfoes.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.magicteam.relicsandfoes.RelicsAndFoes;

@SuppressWarnings("unused")
public final class RAFSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, RelicsAndFoes.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_KYLIN_HURT = event("mob.kylin_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_CHICKEN_HURT = event("mob.chicken_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_SNACK_HURT = event("mob.snack_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_SNACK_JIAO = event("mob.snack_jiao");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_WOLF_HURT = event("mob.wolf_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_COW_HURT = event("mob.cow_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_COW_DEAD = event("mob.cow_dead");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_COW_JIAO = event("mob.cow_jiao");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_IRON_HURT2 = event("mob.iron_hurt2");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_IRON_HIT = event("mob.iron_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_MISSIONARY_HURT = event("mob.missionary_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BELIEVER_HURT = event("mob.believer_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_ANGEL_HURT = event("mob.angel_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_WOOD_HURT = event("mob.wood_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_CLOTH_HURT = event("mob.cloth_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_CONDUCTOR_HURT = event("mob.conductor_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_HIT = event("mob.boss5_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_MECH_DEATH = event("mob.boss5_mech_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_MECH_DEATH_EXPLODE = event("mob.boss5_mech_death_explode");

    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BOSS_KYLIN = sound("music.boss_kylin");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BIOME_KYLIN = sound("music.biome_kylin");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BOSS_ANGEL = sound("music.boss_angel");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BIOME_ANGEL = sound("music.biome_angel");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BOSS_CONDUCTOR = sound("music.boss_conductor");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BIOME_CONDUCTOR = sound("music.biome_conductor");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BOSS_PROTOTYPE_1 = sound("music.boss_prototype_1");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BOSS_PROTOTYPE_4 = sound("music.boss_prototype_4");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BIOME_PROTOTYPE = sound("music.biome_prototype");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BIOME_PROTOTYPE_WIND = sound("music.biome_prototype_wind");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BOSS_MECHANICAL_0 = sound("music.boss_mechanical_0");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BOSS_MECHANICAL_1 = sound("music.boss_mechanical_1");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BIOME_MECHANICAL = sound("music.biome_mechanical");

    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_SWING_BIG = event("mob.swing_big");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_SWING = event("mob.swing");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_SWORD = event("mob.sword");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_WATER_COLUMN = event("mob.water_column");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_AXE_SWING = event("mob.axe_swing");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_CLOCK = event("mob.clock");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_GOD_HURT = event("mob.god_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_IRON_HURT = event("mob.iron_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_STRONGATTACK_SWING = event("mob.strongattack_swing");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_ATTACK_SWING = event("mob.attack_swing");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_KYLIN_BELLOW = event("mob.kylin_bellow");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_LAND = event("mob.land");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_WAVE = event("mob.wave");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_WATER_BLADE = event("mob.water_blade");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_CHEST_OPEN = event("mob.chest_open");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_CHEST_OPEN_CRACK = event("mob.chest_open_crack");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BIG_LAND = event("mob.big_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BALL_HIT = event("mob.ball_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_CLOTH_ATTACK2 = event("mob.cloth_attack2");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_CLOTH_ATTACK = event("mob.cloth_attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_CLOTH_LAND = event("mob.cloth_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_GIFT_BOOM = event("mob.gift_boom");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_FIRE_BALL2 = event("mob.fire_ball2");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_FIRE_BALL1 = event("mob.fire_ball1");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BIG_WAVE_LONG = event("mob.big_wave_long");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BIG_RAIN = event("mob.big_rain");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_THUNDER = event("mob.thunder");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BIG_WAVE = event("mob.big_wave");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_DESERT_WIND = event("mob.desert_wind");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_HUANGCHONG = event("mob.huangchong");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_MAGIC_BIG_SHOOT = event("mob.magic_big_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_FAL_STAR_2 = event("mob.fal_star_2");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_MAGIC_WAVE = event("mob.magic_wave");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_MAGIC_TP = event("mob.magic_tp");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_FALL_STAR = event("mob.fall_star");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_MAGIC_BOMB = event("mob.magic_bomb");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_MAGIC_SHOOT = event("mob.magic_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_FLY_WIND = event("mob.fly_wind");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_MANY_BULLET = event("mob.many_bullet");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_IRON_WIND = event("mob.iron_wind");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_GLIDE = event("mob.glide");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_LASER_OPEN = event("mob.laser_open");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_LIFTING_PLATFORM = event("mob.lifting_platform");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_PRIEST_SMOKE = event("mob.priest_smoke");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_ROCKFALL = event("mob.rockfall");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_CITY_WIND = event("mob.city_wind");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_LASER_CLOSE = event("mob.laser_close");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_DISCOVER = event("mob.discover");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_MSL_EXPLODE = event("mob.boss5_msl_explode");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_MSL_FLYBY = event("mob.boss5_msl_flyby");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_MSL_IR_SEEK = event("mob.boss5_msl_ir_seek");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_MSL_LAUNCH = event("mob.boss5_msl_launch");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_MSL_LOOP = event("mob.boss5_msl_loop");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_RWR_MSL = event("mob.boss5_rwr_msl");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_THUNDER = event("mob.boss5_thunder");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_WIND = event("mob.boss5_wind");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_WINDUP = event("mob.boss5_windup");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_GATE_SHUT = event("mob.boss5_gate_shut");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_GATE_OPEN = event("mob.boss5_gate_open");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_WYVERN_SCREAM = event("mob.boss5_wyvern_scream");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_COGS = event("mob.boss5_cogs");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_STEAM = event("mob.boss5_steam");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOB_BOSS5_CHAINSAW = event("mob.boss5_chainsaw");

    public static final DeferredHolder<SoundEvent, SoundEvent> MESSAGE_BOSS = event("message.boss");
    public static final DeferredHolder<SoundEvent, SoundEvent> MESSAGE_DEFEND_START = event("message.defend_start");
    public static final DeferredHolder<SoundEvent, SoundEvent> MESSAGE_DEFEND_WIN = event("message.defend_win");
    public static final DeferredHolder<SoundEvent, SoundEvent> MESSAGE_DETECTED = event("message.detected");
    public static final DeferredHolder<SoundEvent, SoundEvent> MESSAGE_FINAL = event("message.final");
    public static final DeferredHolder<SoundEvent, SoundEvent> MESSAGE_OVERLOAD = event("message.overload");
    public static final DeferredHolder<SoundEvent, SoundEvent> MESSAGE_PROTECT = event("message.protect");
    public static final DeferredHolder<SoundEvent, SoundEvent> MESSAGE_RUNAWAY_START = event("message.runaway_start");

    private static DeferredHolder<SoundEvent, SoundEvent> sound(String name) {
        return SOUND_EVENTS.register(name, SoundEvent::createVariableRangeEvent);
    }

    private static DeferredHolder<SoundEvent, SoundEvent> event(String name) {
        return SOUND_EVENTS.register(name, id -> SoundEvent.createFixedRangeEvent(id, 16));
    }
}
