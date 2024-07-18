package net.whgkswo.tesm.general;

import net.whgkswo.tesm.conversation.v2.NpcDialogues;
import net.whgkswo.tesm.conversation.v2.dialogues.IndurionDL;

import java.util.HashMap;
import java.util.Map;

public class GlobalVariablesClient {
    public static final Map<String, NpcDialogues> NPC_DIALOGUES = new HashMap<>(){{
        put("인두리온", new IndurionDL());
    }};
}
