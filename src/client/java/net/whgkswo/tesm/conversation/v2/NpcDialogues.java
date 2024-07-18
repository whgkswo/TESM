package net.whgkswo.tesm.conversation.v2;

import net.whgkswo.tesm.conversation.v2.dialogues.IndurionDL;
import net.whgkswo.tesm.general.GlobalVariablesClient;

import java.util.HashMap;
import java.util.Map;

public class NpcDialogues {
    private HashMap<String, NormalStage> normalLines;
    private HashMap<String, DecisionStage> decisions;

    public HashMap<String, NormalStage> getNormalLines() {
        return normalLines;
    }

    public HashMap<String, DecisionStage> getDecisions() {
        return decisions;
    }

    public NpcDialogues(HashMap<String, NormalStage> normalLines, HashMap<String, DecisionStage> decisions) {
        this.normalLines = normalLines;
        this.decisions = decisions;
    }
}
