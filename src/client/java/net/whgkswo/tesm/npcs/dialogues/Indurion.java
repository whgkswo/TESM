package net.whgkswo.tesm.npcs.dialogues;

import net.whgkswo.tesm.npcs.NpcDialogues;

import static net.whgkswo.tesm.npcs.NpcDialogues.*;

public class Indurion {
    public static void registerDL(){
        // 대사 -> [0]: 실행문(([0]유형, [1]대상)x반복), [1]: 대사(라인x반복)
        // 선택지 -> [0]: 선택지(([0]조건,[1]텍스트,[2]선택여부, [3]연결될 대사 번호)x반복)
        //TODO:
        // - 선택지 조건 다양화

        NpcDialogues.npcName = "인두리온";

        putDL("대사","1", new String[][]{{"선택형"},{"테스트 대사입니다.","테스트 대사입니다2", "테스트 대사입니다3"}});
        putDL("선택지","1", new String[][]{{"","(회귀형:1) 테스트 선택지","미선택","1"},{"","(선택형) 테스트 선택지2","미선택","2"},
                {"","(종료형) 테스트 선택지3","미선택","3"}, {"","(종료형) 테스트 선택지4","미선택","4"}, {"테스트 퀘스트 2:시작 가능","(임무형) 테스트 퀘스트 2","미선택","ex1"}});
        putDL("대사","1-1", new String[][]{{"회귀형","1"},{"1번 선택지를 골랐습니다.","1번 선택지를 골랐습니다.2"}});
        putDL("대사","1-2", new String[][]{{"선택형"},{"2번 선택지를 골랐습니다.","2번 선택지를 골랐습니다.2"}});
        putDL("선택지","1-2", new String[][]{{"","(회귀형:1) 1번 선택지","미선택","1"},{"","(회귀형:1-2) 2번 선택지","미선택","2"},
                {"테스트 퀘스트:시작 가능","(퀘스트 시작) 테스트 퀘스트","미선택","ex1"}});
        putDL("대사","1-2-1", new String[][]{{"회귀형","1"},{"1번 선택지를 골랐습니다.","1번 선택지를 골랐습니다.2"}});
        putDL("대사","1-2-2", new String[][]{{"회귀형","1-2"},{"2번 선택지를 골랐습니다.","2번 선택지를 골랐습니다.2"}});
        putDL("대사","1-2-ex1", new String[][]{{"임무형","시작","테스트 퀘스트"},{"테스트 퀘스트를 받겠습니까?","군단장 바로에게 가세요."}});
        putDL("대사","1-3", new String[][]{{"종료형","즉시"},{"3번 선택지를 골랐습니다.","3번 선택지를 골랐습니다.2"}});
        putDL("대사","1-4", new String[][]{{"종료형","마지막"},{"4번 선택지를 골랐습니다.","4번 선택지를 골랐습니다.2"}});
        putDL("대사","1-ex1", new String[][]{{"임무형","시작","테스트 퀘스트 2"},{"5번 선택지를 골랐습니다.","5번 선택지를 골랐습니다.2"}});
    }
}
