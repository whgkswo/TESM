package net.whgkswo.tesm.conversation.dialogues;

import net.whgkswo.tesm.conversation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class IndurionDL extends NpcDialogues {
    public IndurionDL() {
        super(registerNormalLines(), registerDecisions());
    }
    public static void registerGeneralLines(HashMap<String, NormalStage> normalLines){
        normalLines.put("General",
                new NormalStage(new ArrayList<>(Arrays.asList(
                        new NormalLine("테스트 대사입니다."),
                        new NormalLine("테스트 대사입니다.(2)"),
                        new NormalLine("테스트 대사입니다.(3)"))),
                        NormalStage.ExecuteAfter.SHOW_DECISIONS));
    }
    public static HashMap<String, NormalStage> registerNormalLines(){
        HashMap<String, NormalStage> normalLines = new HashMap<>();
        // 범용 일반 대사 등록
        registerGeneralLines(normalLines);
        // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 선택지에 따른 후속 대사 등록ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        registerDecisionLines(normalLines,"General",
                new NormalStage(new ArrayList<>(Arrays.asList(
                        new NormalLine("1번 선택지를 골랐습니다."),
                        new NormalLine("[General]로 돌아갑니다."))),
                        NormalStage.ExecuteAfter.JUMP_TO,
                        "General"),
                new NormalStage(new ArrayList<>(Arrays.asList(
                        new NormalLine("2번 선택지를 골랐습니다."),
                        new NormalLine("추가 선택지를 출력합니다."))),
                        NormalStage.ExecuteAfter.SHOW_DECISIONS),
                new NormalStage(new ArrayList<>(Arrays.asList(
                        new NormalLine("3번 선택지를 골랐습니다."),
                        new NormalLine("대화를 종료합니다."))),
                        NormalStage.ExecuteAfter.EXIT)
                );
        registerDecisionLines(normalLines, "General-2",
                new NormalStage(new ArrayList<>(Arrays.asList(
                        new NormalLine("저는 인두리온입니다.", true),
                        new NormalLine("저기 밑 부둣가 행정 사무소가 제 직장이죠.")
                )), NormalStage.ExecuteAfter.JUMP_TO, "General-2"),
                new NormalStage(new ArrayList<>(Arrays.asList(
                        new NormalLine("2번 선택지를 골랐습니다."),
                        new NormalLine("[General]로 돌아갑니다."))),
                        NormalStage.ExecuteAfter.JUMP_TO,
                        "General"),
                new NormalStage(new ArrayList<>(Arrays.asList(
                        new NormalLine("3번 선택지를 골랐습니다."),
                        new NormalLine("[General-2]로 돌아갑니다."))),
                        NormalStage.ExecuteAfter.JUMP_TO,
                        "General-2")
                );
        return normalLines;
    }
    public static HashMap<String, DecisionStage> registerDecisions(){
        HashMap<String, DecisionStage> decisions = new HashMap<>();
        decisions.put("General", new DecisionStage(new ArrayList<>(Arrays.asList(
                new Decision("(반복)"),
                new Decision("계속 말해보시오"),
                new Decision("그럼 이만")
        ))));
        decisions.put("General-2", new DecisionStage(new ArrayList<>(Arrays.asList(
                new Decision("당신에 대해 말해주시오"),
                new Decision("아까 얘기로 돌아가서..."),
                new Decision("(반복)")
        ))));
        return decisions;
    }
}
