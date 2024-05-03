package net.whgkswo.tesm.npcs;

import net.whgkswo.tesm.npcs.dialogues.Indurion;
import net.whgkswo.tesm.npcs.dialogues.LegatePreciliusVaro;

import java.util.HashMap;
import java.util.Map;

public class NpcDialogues {
    public static Map<String,String[][]> npcDLMap = new HashMap<>();
    public static String npcName;

    public static void putDL(String type, String stage, String[][] data){
        npcDLMap.put(npcName + "_" + type + "_" + stage, data);
    }
    public static void npcDialoues(){
        Indurion.registerDL();
        LegatePreciliusVaro.registerDL();
    }
}
