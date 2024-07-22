package net.whgkswo.tesm.conversation;

import java.util.HashMap;

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
    public static void registerDecisionLines(HashMap<String, NormalStage> normalLines, String sourceStage,
                                             NormalStage... normalStage){
        for(int i = 0; i< normalStage.length; i++){
            normalLines.put(sourceStage + "-" + (i+1), normalStage[i]);
        }
    }
}
