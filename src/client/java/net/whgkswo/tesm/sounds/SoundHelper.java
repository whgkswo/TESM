package net.whgkswo.tesm.sounds;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.whgkswo.tesm.general.GlobalVariables;

public class SoundHelper {
    public static void playSound(SoundEvent sound, SoundCategory category){
        GlobalVariables.world.playSound(null, GlobalVariables.player.getBlockPos(), sound, category ,1f, 1f);
    }
}
