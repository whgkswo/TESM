package net.whgkswo.tesm.conversation.v2.dialogues;

import net.whgkswo.tesm.conversation.v2.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static net.whgkswo.tesm.conversation.v2.DialogueType.DECISIONS;
import static net.whgkswo.tesm.conversation.v2.DialogueType.NORMAL;

public class IndurionDL extends NpcDialogues {
    public IndurionDL() {
        super(registerNormalLines(), registerDecisions());
    }
    public static HashMap<String, NormalStage> registerNormalLines(){
        HashMap<String, NormalStage> normalLines = new HashMap<>();
        normalLines.put("1",
                new NormalStage(NORMAL, new ArrayList<>(Arrays.asList(
                        "테스트 대사입니다.",
                        "테스트 대사입니다2",
                        "테스트 대사입니다3")),
                        NormalStage.ExecuteAfter.SHOW_DECISIONS));
        return normalLines;
    }
    public static HashMap<String, DecisionStage> registerDecisions(){
        HashMap<String, DecisionStage> decisions = new HashMap<>();
        decisions.put("1", new DecisionStage(DECISIONS, new ArrayList<>(Arrays.asList(
                new Decision("테스트 선택지"),
                new Decision("테스트 선택지2"),
                new Decision("테스트 선택지3"),
                new Decision("테스트 선택지4"),
                new Decision("테스트 선택지5")
        ))));
        return decisions;
    }
}
