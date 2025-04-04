package net.whgkswo.tesm.message;

import net.minecraft.text.Text;
import net.whgkswo.tesm.general.GlobalVariables;

public class MessageHelper {
    public static void sendMessage(String message){
        GlobalVariables.player.sendMessage(Text.literal(message), false);
    }

    public static void sendMessage(String message, boolean overlay){
        GlobalVariables.player.sendMessage(Text.literal(message), overlay);
    }

    public static void sendMessage(Text text){
        GlobalVariables.player.sendMessage(text, false);
    }

    public static void sendMessage(Text text, boolean overlay){
        GlobalVariables.player.sendMessage(text, overlay);
    }
}
