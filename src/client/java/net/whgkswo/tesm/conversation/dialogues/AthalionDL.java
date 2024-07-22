package net.whgkswo.tesm.conversation.dialogues;

import net.whgkswo.tesm.conversation.*;
import net.whgkswo.tesm.conversation.quest.QuestStatus;

import java.util.HashMap;

public class AthalionDL extends NpcDialogues {
    public AthalionDL() {
        super(registerNormalLines(), registerDecisions());
    }
    public static void registerGeneralLines(HashMap<String, NormalStage> normalLines){
        normalLines.put("General",
                new NormalStage(NormalStage.ExecuteAfter.SHOW_DECISIONS,
                        new NormalLine("테스트 대사입니다."),
                        new NormalLine("테스트 대사입니다.(2)"),
                        new NormalLine("테스트 대사입니다.(3)")));
    }
    public static HashMap<String, DecisionStage> registerDecisions(){
        HashMap<String, DecisionStage> decisions = new HashMap<>();
        decisions.put("General", new DecisionStage(
                new Decision("인두리온이 보내서 왔습니다.", new QuestRequirement("테스트 퀘스트", QuestStatus.ONGOING))
        ));
        return decisions;
    }
    public static HashMap<String, NormalStage> registerNormalLines(){
        HashMap<String, NormalStage> normalLines = new HashMap<>();
        // 범용 일반 대사 등록
        registerGeneralLines(normalLines);
        // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 선택지에 따른 후속 대사 등록ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        registerDecisionLines(normalLines,"General",
                new NormalStage(NormalStage.ExecuteAfter.COMPLETE_QUEST, "테스트 퀘스트",
                        new NormalLine("인두리온이 말한 사람이 당신이었군요."),
                        new NormalLine("여기 보수입니다."))
        );
        return normalLines;
    }
}
