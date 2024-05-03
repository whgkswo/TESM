package net.whgkswo.tesm.npcs.dialogues;

import net.whgkswo.tesm.npcs.NpcDialogues;

import static net.whgkswo.tesm.npcs.NpcDialogues.*;

public class LegatePreciliusVaro {
    public static void registerDL(){ // npcDialogues.npcDialoues()에 등록해줘야 함!
        NpcDialogues.npcName = "군단장 프레실리우스 바로";

        putDL("대사","1", new String[][]{{"선택형"},{"테스트 대사입니다.","테스트 대사입니다2", "테스트 대사입니다3"}});
        putDL("선택지","1", new String[][]{{"테스트 퀘스트:진행중","(임무 완료) 테스트 퀘스트","미선택","ex1"}});
        putDL("대사","1-ex1", new String[][]{{"임무형","완료","테스트 퀘스트"},{"테스트 대사입니다."}});
    }
}
