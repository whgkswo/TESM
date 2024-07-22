package net.whgkswo.tesm.general;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.whgkswo.tesm.conversation.NpcDialogues;
import net.whgkswo.tesm.conversation.dialogues.AthalionDL;
import net.whgkswo.tesm.conversation.dialogues.IndurionDL;

import java.util.HashMap;
import java.util.Map;

public class GlobalVariablesClient {
    public static String convPartnerTempName;
    public static String convPartnerName;
    public static int arrowCounter = 0;
    public static boolean arrowState = false;
    public static final Map<String, NpcDialogues> NPC_DIALOGUES = new HashMap<>(){{
        put("인두리온", new IndurionDL());
        put("아탈리온", new AthalionDL());
    }};
    public static int screenWidth = MinecraftClient.getInstance().getWindow().getWidth();
    public static int screenHeight = MinecraftClient.getInstance().getWindow().getHeight();
}
