package net.whgkswo.tesm.conversation.v2.dialogues;

import net.whgkswo.tesm.conversation.v2.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class IndurionDL extends NpcDialogues {
    public IndurionDL() {
        super(registerNormalLines(), registerDecisions());
    }
    public static HashMap<String, NormalStage> registerNormalLines(){
        HashMap<String, NormalStage> normalLines = new HashMap<>();
        normalLines.put("General",
                new NormalStage(new ArrayList<>(Arrays.asList(
                        "테스트 대사입니다.",
                        "테스트 대사입니다.(2)",
                        "테스트 대사입니다.(3)")),
                        NormalStage.ExecuteAfter.SHOW_DECISIONS));
        normalLines.put("General-1",
                new NormalStage(new ArrayList<>(Arrays.asList(
                        "1번 선택지를 골랐습니다.",
                        "[General]로 돌아갑니다.")),
                        NormalStage.ExecuteAfter.JUMP_TO,
                        "General"));
        normalLines.put("General-2",
                new NormalStage(new ArrayList<>(Arrays.asList(
                        "2번 선택지를 골랐습니다.",
                        "추가 선택지를 출력합니다.")),
                        NormalStage.ExecuteAfter.SHOW_DECISIONS));
        normalLines.put("General-2-1",
                new NormalStage(new ArrayList<>(Arrays.asList(
                        "1번 선택지를 골랐습니다.",
                        "[General]로 돌아갑니다.")),
                        NormalStage.ExecuteAfter.JUMP_TO,
                        "General"));
        normalLines.put("General-2-2",
                new NormalStage(new ArrayList<>(Arrays.asList(
                        "1번 선택지를 골랐습니다.",
                        "[General-2]로 돌아갑니다.")),
                        NormalStage.ExecuteAfter.JUMP_TO,
                        "General-2"));
        normalLines.put("General-3",
                new NormalStage(new ArrayList<>(Arrays.asList(
                        "3번 선택지를 골랐습니다.",
                        "대화를 종료합니다.")),
                        NormalStage.ExecuteAfter.EXIT));
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
                new Decision("아까 얘기로 돌아가서..."),
                new Decision("(반복)")
        ))));
        return decisions;
    }
}
