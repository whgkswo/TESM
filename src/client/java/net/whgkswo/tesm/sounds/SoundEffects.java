package net.whgkswo.tesm.sounds;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;

public class SoundEffects {
    public static final SoundEvent QUEST_START = SoundEvent.of(new Identifier(TESMMod.MODID, "new_quest"));
    public static final SoundEvent NEW_OBJECTIVE = SoundEvent.of(new Identifier(TESMMod.MODID, "new_objective_1"));
    public static final SoundEvent QUEST_COMPLETED = SoundEvent.of(new Identifier(TESMMod.MODID, "quest_completed"));
}
