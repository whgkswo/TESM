package net.whgkswo.tesm.conversation.dialogues;

import net.whgkswo.tesm.conversation.*;
import net.whgkswo.tesm.conversation.quest.QuestStatus;

import java.util.HashMap;

public class IndurionDL extends NpcDialogues {
    public IndurionDL() {
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
                new Decision("(반복)"),
                new Decision("계속 말해보시오"),
                new Decision("그럼 이만")
        ));
        decisions.put("General-2", new DecisionStage(
                new Decision("당신에 대해 말해주시오"),
                new Decision("아까 얘기로 돌아가서..."),
                new Decision("(반복)"),
                new Decision("퀘스트를 받겠습니다.", new QuestRequirement("테스트 퀘스트", QuestStatus.AVAILABLE))
        ));
        return decisions;
    }
    public static HashMap<String, NormalStage> registerNormalLines(){
        HashMap<String, NormalStage> normalLines = new HashMap<>();
        // 범용 일반 대사 등록
        registerGeneralLines(normalLines);
        // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 선택지에 따른 후속 대사 등록ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        registerDecisionLines(normalLines,"General",
                new NormalStage(NormalStage.ExecuteAfter.JUMP_TO, "General",
                        new NormalLine("1번 선택지를 골랐습니다."),
                        new NormalLine("[General]로 돌아갑니다.")),
                new NormalStage(NormalStage.ExecuteAfter.SHOW_DECISIONS,
                        new NormalLine("2번 선택지를 골랐습니다."),
                        new NormalLine("추가 선택지를 출력합니다.")),
                new NormalStage(NormalStage.ExecuteAfter.CLOSE,
                        new NormalLine("3번 선택지를 골랐습니다."),
                        new NormalLine("대화를 종료합니다."))
                );
        registerDecisionLines(normalLines, "General-2",
                new NormalStage(NormalStage.ExecuteAfter.JUMP_TO, "General-2",
                        new NormalLine("저는 인두리온입니다.", true),
                        new NormalLine("저기 밑 부둣가 행정 사무소가 제 직장이죠.")),
                new NormalStage(NormalStage.ExecuteAfter.JUMP_TO, "General",
                        new NormalLine("2번 선택지를 골랐습니다."),
                        new NormalLine("[General]로 돌아갑니다.")),
                new NormalStage(NormalStage.ExecuteAfter.JUMP_TO, "General-2",
                        new NormalLine("3번 선택지를 골랐습니다."),
                        new NormalLine("[General-2]로 돌아갑니다.")),
                new NormalStage(NormalStage.ExecuteAfter.START_QUEST, "테스트 퀘스트",
                        new NormalLine("퀘스트를 받겠습니까?"),
                        new NormalLine("아탈리온과 이야기하세요."))
                );
        return normalLines;
    }
}
