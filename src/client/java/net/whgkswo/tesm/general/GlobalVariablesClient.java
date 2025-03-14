package net.whgkswo.tesm.general;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.whgkswo.tesm.conversation.NpcDialogues;
import net.whgkswo.tesm.conversation.dialogues.AthalionDL;
import net.whgkswo.tesm.conversation.dialogues.IndurionDL;
import net.whgkswo.tesm.conversation.dialogues.OktoKamarroDL;
import net.whgkswo.tesm.sounds.musics.MusicPlayer;

import java.util.HashMap;
import java.util.Map;

public class GlobalVariablesClient {
    public static MusicPlayer musicPlayer;
    public static String convPartnerTempName;
    public static String convPartnerName;
    public static int arrowCounter = 0;
    public static boolean arrowState = false;
    public static final Map<String, NpcDialogues> NPC_DIALOGUES = new HashMap<>(){{
        put("인두리온", new IndurionDL());
        put("아탈리온", new AthalionDL());
        put("옥토-카마로", new OktoKamarroDL());
    }};
}
