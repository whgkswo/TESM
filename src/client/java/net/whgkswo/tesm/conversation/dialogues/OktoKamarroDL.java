package net.whgkswo.tesm.conversation.dialogues;

import net.whgkswo.tesm.conversation.*;
import net.whgkswo.tesm.conversation.quest.QuestStatus;

import java.util.HashMap;

public class OktoKamarroDL extends NpcDialogues {
    public OktoKamarroDL() {
        super(registerNormalLines(), registerDecisions());
    }
    public static void registerGeneralLines(HashMap<String, NormalStage> normalLines){
        normalLines.put("General",
                new NormalStage(NormalStage.ExecuteAfter.SHOW_DECISIONS,
                        new NormalLine("반갑다. 너도 여행자인가?"),
                        new NormalLine("너한테서 바람 냄새가 난다.")
                ));
    }
    public static HashMap<String, DecisionStage> registerDecisions(){
        HashMap<String, DecisionStage> decisions = new HashMap<>();
        decisions.put("General", new DecisionStage(
                new Decision("[테스트 퀘스트 2] 인두리온이 보내서 왔습니다.",
                        new QuestRequirement("테스트 퀘스트 2", QuestStatus.ONGOING, "분기점 1"), "옥토 카마로 루트")
        ));
        return decisions;
    }
    public static HashMap<String, NormalStage> registerNormalLines(){
        HashMap<String, NormalStage> normalLines = new HashMap<>();
        // 범용 일반 대사 등록
        registerGeneralLines(normalLines);
        // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 선택지에 따른 후속 대사 등록ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        registerDecisionLines(normalLines,"General",
                new NormalStage(NormalStage.ExecuteAfter.ADVANCE_QUEST, "테스트 퀘스트 2","결말",
                        new NormalLine("인두리온이 말했던 사람이 너구나!"),
                        new NormalLine("자, 여기 보수다."))
        );
        return normalLines;
    }

}
